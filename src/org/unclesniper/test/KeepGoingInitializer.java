package org.unclesniper.test;

public class KeepGoingInitializer<BaseT> implements Initializer<BaseT> {

	private final Initializer<? super BaseT> initializer;

	private final boolean keepGoing;

	public KeepGoingInitializer(Initializer<? super BaseT> initializer, boolean keepGoing) {
		if(initializer == null)
			throw new IllegalArgumentException("Initializer must not be null");
		this.initializer = initializer;
		this.keepGoing = keepGoing;
	}

	public Initializer<? super BaseT> getInitializer() {
		return initializer;
	}

	public boolean isKeepGoing() {
		return keepGoing;
	}

	@Override
	public void initializeBase(TestContext context, BaseT base, String baseName, boolean keepGoing,
			TestResultSink sink, boolean finalizing) throws Throwable {
		initializer.initializeBase(context, base, baseName, this.keepGoing, sink, finalizing);
	}

}
