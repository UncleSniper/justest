package org.unclesniper.test;

import java.io.IOException;

public interface Testable<BaseT> {

	void performTest(TestContext context, BaseT base, TestResultSink sink) throws IOException;

	public static void run(TestContext context, TestResultSink sink, Testable<Void> test) throws IOException {
		if(test == null)
			throw new IllegalArgumentException("Test must not be null");
		test.performTest(context == null ? EmptyTestContext.instance : context, null, sink);
	}

}
