package org.unclesniper.test;

import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import java.util.stream.Stream;

public class MultiInitializer<BaseT> implements Initializer<BaseT> {

	private final List<Initializer<? super BaseT>> initializers = new LinkedList<Initializer<? super BaseT>>();

	public MultiInitializer() {}

	public MultiInitializer(Iterable<? extends Initializer<? super BaseT>> initializers) {
		if(initializers != null) {
			for(Initializer<? super BaseT> initializer : initializers) {
				if(initializer != null)
					this.initializers.add(initializer);
			}
		}
	}

	public MultiInitializer(Stream<? extends Initializer<? super BaseT>> initializers) {
		if(initializers != null) {
			initializers.forEach(initializer -> {
				if(initializer != null)
					this.initializers.add(initializer);
			});
		}
	}

	public List<Initializer<? super BaseT>> getInitializers() {
		return Collections.unmodifiableList(initializers);
	}

	public void addInitializer(Initializer<? super BaseT> initializer) {
		if(initializer != null)
			initializers.add(initializer);
	}

	public void initializeBase(TestContext context, BaseT base, String baseName, boolean keepGoing,
			TestResultSink sink, boolean finalizing) throws Throwable {
		MultiInitializer.runMultipleInitializers(initializers, context, base, baseName, keepGoing, sink, finalizing);
	}

	public boolean isRequired() {
		for(Initializer<? super BaseT> initializer : initializers) {
			if(initializer.isRequired())
				return true;
		}
		return false;
	}

	public static <BaseT> void runMultipleInitializers(Iterable<? extends Initializer<? super BaseT>> initializers,
			TestContext context, BaseT base, String baseName, boolean keepGoing, TestResultSink sink,
			boolean finalizing) throws Throwable {
		List<Throwable> errors = new LinkedList<Throwable>();
		boolean requiredFailed = false, broken = false;
		if(initializers != null) {
			for(Initializer<? super BaseT> initializer : initializers) {
				if(initializer == null)
					continue;
				if(broken) {
					if(initializer.isRequired()) {
						requiredFailed = true;
						break;
					}
				}
				else {
					try {
						initializer.initializeBase(context, base, baseName, keepGoing, sink, finalizing);
					}
					catch(Throwable t) {
						errors.add(t);
						requiredFailed = requiredFailed || initializer.isRequired();
						if(!keepGoing) {
							broken = true;
							if(requiredFailed)
								break;
						}
					}
				}
			}
		}
		if(requiredFailed) {
			IllegalStateException ise = new IllegalStateException("Compound initialization "
					+ (baseName == null ? "failed" : "of '" + baseName + "' failed"));
			for(Throwable error : errors)
				ise.addSuppressed(error);
			throw ise;
		}
	}

}
