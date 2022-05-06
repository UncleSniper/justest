package org.unclesniper.test;

import java.io.IOException;

import static org.unclesniper.test.TestUtils.notNull;

public interface Testable<BaseT> {

	void performTest(TestContext context, BaseT base, TestResultSink sink) throws IOException;

	public static void run(TestContext context, TestResultSink sink, Testable<Void> test) throws IOException {
		notNull(test, "Test");
		if(sink != null)
			sink.beginRun();
		test.performTest(context == null ? EmptyTestContext.instance : context, null, sink);
		if(sink != null)
			sink.endRun();
	}

}
