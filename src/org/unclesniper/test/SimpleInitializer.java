package org.unclesniper.test;

import static org.unclesniper.test.TestUtils.notNull;

public class SimpleInitializer<BaseT> implements Initializer<BaseT> {

	private final ContextInitializer<? super BaseT> initializer;

	private final boolean required;

	public SimpleInitializer(ContextInitializer<? super BaseT> initializer) {
		this(initializer, true);
	}

	public SimpleInitializer(ContextInitializer<? super BaseT> initializer, boolean required) {
		this.initializer = notNull(initializer, "Initializer");
		this.required = required;
	}

	@Override
	public void initializeBase(TestContext context, BaseT base, String baseName, boolean keepGoing,
			TestResultSink sink, boolean finalizing) throws Throwable {
		RefCell<Throwable> except = new RefCell<Throwable>();
		CapturedOutput capturedOutput;
		try {
			capturedOutput = TestUtils.captureOutput(() -> initializer.initializeBase(context, base),
					except::setValue);
		}
		catch(Throwable t) {
			if(t instanceof Error)
				throw (Error)t;
			if(t instanceof RuntimeException)
				throw (RuntimeException)t;
			throw new Error("Exception should not have been possible to throw", t);
		}
		Throwable thrown = except.getValue();
		SimpleInitializationResult result = new SimpleInitializationResult(base, thrown, capturedOutput);
		if(sink != null) {
			if(finalizing)
				sink.finalizationResult(baseName, result, required);
			else
				sink.initializationResult(baseName, result, required);
		}
		if(thrown != null)
			throw thrown;
	}

	@Override
	public boolean isRequired() {
		return required;
	}

}
