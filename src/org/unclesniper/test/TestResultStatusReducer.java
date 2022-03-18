package org.unclesniper.test;

import java.util.function.Consumer;

public class TestResultStatusReducer implements Consumer<TestResult> {

	private TestStatus status = TestStatus.SKIPPED;

	private boolean hadAny;

	public TestResultStatusReducer() {}

	public TestStatus getStatus() {
		return hadAny ? status : TestStatus.PASSED;
	}

	@Override
	public void accept(TestResult result) {
		TestStatus childStatus = result == null ? null : result.getStatus();
		if(childStatus == null)
			return;
		hadAny = true;
		switch(childStatus) {
			case PASSED:
				if(status == TestStatus.SKIPPED)
					status = TestStatus.PASSED;
				break;
			case SKIPPED:
				break;
			case FAILED:
				status = TestStatus.FAILED;
				break;
			default:
				throw new Error("Unrecognized TestStatus: " + childStatus.name());
		}
	}

}
