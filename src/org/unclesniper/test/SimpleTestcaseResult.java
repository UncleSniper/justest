package org.unclesniper.test;

import java.util.stream.Stream;

public class SimpleTestcaseResult extends AbstractTestResult implements TestcaseResult {

	private final TestStatus status;

	private final AssertionFailureError assertionError;

	private final AssumptionFailureError assumptionError;

	private final CapturedOutput capturedOutput;

	public SimpleTestcaseResult(String name, CapturedOutput capturedOutput) {
		super(name);
		status = TestStatus.PASSED;
		assertionError = null;
		assumptionError = null;
		this.capturedOutput = capturedOutput;
	}

	public SimpleTestcaseResult(String name, AssertionFailureError assertionError,
			CapturedOutput capturedOutput) {
		super(name);
		status = TestStatus.FAILED;
		this.assertionError = assertionError;
		assumptionError = null;
		this.capturedOutput = capturedOutput;
	}

	public SimpleTestcaseResult(String name, AssumptionFailureError assumptionError,
			CapturedOutput capturedOutput) {
		super(name);
		status = TestStatus.SKIPPED;
		assertionError = null;
		this.assumptionError = assumptionError;
		this.capturedOutput = capturedOutput;
	}

	@Override
	public TestStatus getStatus() {
		return status;
	}

	@Override
	public AssertionFailureError getAssertionFailureError() {
		return assertionError;
	}

	@Override
	public AssumptionFailureError getAssumptionFailureError() {
		return assumptionError;
	}

	@Override
	public Stream<String> getStandardOutput() {
		Stream<String> stream = capturedOutput == null ? null : capturedOutput.getStandardOutput();
		return stream == null ? Stream.empty() : stream;
	}

	@Override
	public Stream<String> getStandardError() {
		Stream<String> stream = capturedOutput == null ? null : capturedOutput.getStandardError();
		return stream == null ? Stream.empty() : stream;
	}

}
