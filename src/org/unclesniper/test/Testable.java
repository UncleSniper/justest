package org.unclesniper.test;

import java.io.IOException;

public interface Testable<BaseT> {

	void performTest(TestContext context, BaseT base, TestResultSink sink) throws IOException;

}
