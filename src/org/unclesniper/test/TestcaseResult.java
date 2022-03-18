package org.unclesniper.test;

public interface TestcaseResult extends TestResult, CapturedOutput {

	AssertionFailureError getAssertionFailureError();

	AssumptionFailureError getAssumptionFailureError();

}
