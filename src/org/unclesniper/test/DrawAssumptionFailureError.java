package org.unclesniper.test;

import java.util.stream.Stream;

import static org.unclesniper.test.TestUtils.notNull;

public class DrawAssumptionFailureError extends AssumptionFailureError {

	public DrawAssumptionFailureError(Throwable cause) {
		super("Expected to be able to draw assumption subject from GenSource, but failed with: "
				+ notNull(cause, "Cause"), () -> Stream.of(
					"Expected to be able to draw assumption subject from GenSource, but failed with:",
					"    " + cause
				));
	}

}
