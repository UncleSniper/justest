package org.unclesniper.test;

public interface Initializer<BaseT> {

	void initializeBase(TestContext context, BaseT base, String baseName, boolean keepGoing, TestResultSink sink,
			boolean finalizing) throws Throwable;

	default boolean isRequired() {
		return true;
	}

}
