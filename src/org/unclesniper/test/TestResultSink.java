package org.unclesniper.test;

import java.io.IOException;

public interface TestResultSink {

	void beginTestcase(String name) throws IOException;

	void endTestcase(String name, TestcaseResult result) throws IOException;

	void beginTestsuite(String name) throws IOException;

	void endTestsuite(String name, TestsuiteResult result) throws IOException;

	void initializationResult(String name, InitializationResult result, boolean required) throws IOException;

	void finalizationResult(String name, InitializationResult result, boolean required) throws IOException;

}
