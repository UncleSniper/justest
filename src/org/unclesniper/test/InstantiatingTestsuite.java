package org.unclesniper.test;

import java.util.List;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Collections;
import java.util.stream.Stream;

import static org.unclesniper.test.TestUtils.notNull;

public class InstantiatingTestsuite<BaseT> extends AbstractSimpleTestsuite<Void, BaseT>
		implements FlowInitializedTestsuite<Void, BaseT, InstantiatingTestsuite<BaseT>> {

	private static class InstantiationResult<BaseT> extends SimpleInitializationResult {

		private final BaseT instance;

		public InstantiationResult(Object base, Throwable error, CapturedOutput capturedOutput, BaseT instance) {
			super(base, error, capturedOutput);
			if((error == null) == (instance == null))
				throw new IllegalArgumentException("Exactly one of 'error' or 'instance' must be null");
			this.instance = instance;
		}

		public BaseT getInstance() {
			return instance;
		}

	}

	private final ObjectFactory<? extends BaseT, ? extends Throwable> constructor;

	private final List<Initializer<? super BaseT>> initializers = new LinkedList<Initializer<? super BaseT>>();

	private final List<Initializer<? super BaseT>> finalizers = new LinkedList<Initializer<? super BaseT>>();

	private final List<Initializer<? super BaseT>> caseInitializers = new LinkedList<Initializer<? super BaseT>>();

	private final List<Initializer<? super BaseT>> caseFinalizers = new LinkedList<Initializer<? super BaseT>>();

	private boolean reinstantiateForEachTestcase;

	public InstantiatingTestsuite(String name, ObjectFactory<? extends BaseT, ? extends Throwable> constructor) {
		super(name);
		this.constructor = notNull(constructor, "Constructor");
	}

	public InstantiatingTestsuite(String name, ObjectFactory<? extends BaseT, ? extends Throwable> constructor,
			Iterable<? extends Testable<? super BaseT>> tests) {
		super(name, tests);
		this.constructor = notNull(constructor, "Constructor");
	}

	public InstantiatingTestsuite(String name, ObjectFactory<? extends BaseT, ? extends Throwable> constructor,
			Stream<? extends Testable<? super BaseT>> tests) {
		super(name, tests);
		this.constructor = notNull(constructor, "Constructor");
	}

	public List<Initializer<? super BaseT>> getInitializers() {
		return Collections.unmodifiableList(initializers);
	}

	public void addInitializer(Initializer<? super BaseT> initializer) {
		if(initializer != null)
			initializers.add(initializer);
	}

	public List<Initializer<? super BaseT>> getFinalizers() {
		return Collections.unmodifiableList(finalizers);
	}

	public void addFinalizer(Initializer<? super BaseT> finalizer) {
		if(finalizer != null)
			finalizers.add(finalizer);
	}

	public List<Initializer<? super BaseT>> getTestcaseInitializers() {
		return Collections.unmodifiableList(caseInitializers);
	}

	public void addTestcaseInitializer(Initializer<? super BaseT> initializer) {
		if(initializer != null)
			caseInitializers.add(initializer);
	}

	public List<Initializer<? super BaseT>> getTestcaseFinalizers() {
		return Collections.unmodifiableList(caseFinalizers);
	}

	public void addTestcaseFinalizer(Initializer<? super BaseT> finalizer) {
		if(finalizer != null)
			caseFinalizers.add(finalizer);
	}

	public boolean isReinstantiateForEachTestcase() {
		return reinstantiateForEachTestcase;
	}

	public void setReinstantiateForEachTestcase(boolean reinstantiateForEachTestcase) {
		this.reinstantiateForEachTestcase = reinstantiateForEachTestcase;
	}

	public InstantiatingTestsuite<BaseT> newEach(boolean reinstantiateForEachTestcase) {
		setReinstantiateForEachTestcase(reinstantiateForEachTestcase);
		return this;
	}

	public InstantiatingTestsuite<BaseT> newEach() {
		setReinstantiateForEachTestcase(true);
		return this;
	}

	public InstantiatingTestsuite<BaseT> newOnce() {
		setReinstantiateForEachTestcase(false);
		return this;
	}

	private InstantiationResult<BaseT> newBase(String name) {
		RefCell<Throwable> except = new RefCell<Throwable>();
		CapturedOutput capturedOutput;
		RefCell<BaseT> instance = new RefCell<BaseT>();
		try {
			capturedOutput = TestUtils.captureOutput(() -> {
				BaseT obj = constructor.newObject();
				if(obj == null)
					throw new IllegalStateException("Constructor returned null");
				instance.setValue(obj);
			}, except::setValue);
		}
		catch(Throwable t) {
			if(t instanceof Error)
				throw (Error)t;
			if(t instanceof RuntimeException)
				throw (RuntimeException)t;
			throw new Error("Exception should not have been possible to throw", t);
		}
		return new InstantiationResult<BaseT>(new InitializationText("Instantiating testsuite"
				+ (name == null ? "" : " '" + name + '\'')), except.getValue(), capturedOutput,
				instance.getValue());
	}

	private static <BaseT> boolean runInitializers(String name, TestContext context, BaseT base,
			TestResultSink sink, Iterable<? extends Initializer<? super BaseT>> initializers, boolean finalizing)
			throws IOException {
		Throwable thrown;
		try {
			MultiInitializer.runMultipleInitializers(initializers, context, base, name, true, sink, finalizing);
			thrown = null;
		}
		catch(Throwable t) {
			thrown = t;
		}
		if(sink != null) {
			boolean anyRequired = false;
			for(Initializer<? super BaseT> initializer : initializers) {
				if(initializer.isRequired()) {
					anyRequired = true;
					break;
				}
			}
			sink.initializationResult(name, new SimpleInitializationResult(base, thrown,
					EmptyCapturedOutput.instance), anyRequired);
		}
		return thrown != null;
	}

	private boolean initializeBase(String name, TestContext context, BaseT base, TestResultSink sink)
			throws IOException {
		return InstantiatingTestsuite.runInitializers(name, context, base, sink, initializers, false);
	}

	private BaseT newInitializedBase(String name, TestContext context, TestResultSink sink) throws IOException {
		InstantiationResult<BaseT> instantiationResult = newBase(name);
		if(sink != null)
			sink.initializationResult(name, instantiationResult, true);
		BaseT instance = instantiationResult.getInstance();
		if(instance != null && initializeBase(name, context, instance, sink))
			instance = null;
		return instance;
	}

	private boolean finalizeBase(String name, TestContext context, BaseT base, TestResultSink sink)
			throws IOException {
		return InstantiatingTestsuite.runInitializers(name, context, base, sink, finalizers, true);
	}

	private boolean initializeCase(String name, TestContext context, BaseT base, TestResultSink sink)
			throws IOException {
		return InstantiatingTestsuite.runInitializers(name, context, base, sink, caseInitializers, false);
	}

	private boolean finalizeCase(String name, TestContext context, BaseT base, TestResultSink sink)
			throws IOException {
		return InstantiatingTestsuite.runInitializers(name, context, base, sink, caseFinalizers, true);
	}

	private void runCase(String name, TestContext context, BaseT base, TestResultSink sink,
			Testable<? super BaseT> testcase) throws IOException {
		if(initializeCase(name, context, base, sink))
			return;
		testcase.performTest(context, base, sink);
		finalizeCase(name, context, base, sink);
	}

	@Override
	protected boolean performSuite(TestContext context, Void base, TestResultSink sink) throws IOException {
		List<Testable<? super BaseT>> tests = getTests();
		if(tests == null)
			return false;
		String name = getName();
		boolean forceFail = false;
		if(reinstantiateForEachTestcase) {
			for(Testable<? super BaseT> test : tests) {
				BaseT instance = newInitializedBase(name, context, sink);
				if(instance == null) {
					forceFail = true;
					continue;
				}
				runCase(name, context, instance, sink, test);
				if(finalizeBase(name, context, instance, sink))
					forceFail = true;
			}
		}
		else {
			BaseT instance = newInitializedBase(name, context, sink);
			if(instance == null)
				forceFail = true;
			else {
				for(Testable<? super BaseT> test : tests)
					runCase(name, context, instance, sink, test);
				if(finalizeBase(name, context, instance, sink))
					forceFail = true;
			}
		}
		return forceFail;
	}

	@Override
	public InstantiatingTestsuite<BaseT> test(Testable<? super BaseT> test) {
		addTest(test);
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public InstantiatingTestsuite<BaseT> initialize(Initializer<? super BaseT>... initializers) {
		if(initializers != null) {
			for(Initializer<? super BaseT> initializer : initializers)
				addInitializer(initializer);
		}
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public InstantiatingTestsuite<BaseT> finalize(Initializer<? super BaseT>... finalizers) {
		if(initializers != null) {
			for(Initializer<? super BaseT> finalizer : finalizers)
				addFinalizer(finalizer);
		}
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public InstantiatingTestsuite<BaseT> initializeEach(Initializer<? super BaseT>... initializers) {
		if(initializers != null) {
			for(Initializer<? super BaseT> initializer : initializers)
				addTestcaseInitializer(initializer);
		}
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public InstantiatingTestsuite<BaseT> finalizeEach(Initializer<? super BaseT>... finalizers) {
		if(initializers != null) {
			for(Initializer<? super BaseT> finalizer : finalizers)
				addTestcaseFinalizer(finalizer);
		}
		return this;
	}

}
