package org.unclesniper.test;

import java.util.stream.Stream;

public interface TestsuiteResult extends TestResult {

	Stream<? extends TestResult> getContainedResults();

}
