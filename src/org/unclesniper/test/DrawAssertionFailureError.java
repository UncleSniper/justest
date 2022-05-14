package org.unclesniper.test;

import java.util.stream.Stream;

import static org.unclesniper.test.TestUtils.notNull;

public class DrawAssertionFailureError extends AssertionFailureError {

	public DrawAssertionFailureError(Throwable cause) {
		super("Expected to be able to draw assertion subject from GenSource, but failed with: "
				+ notNull(cause, "Cause"), () -> Stream.of(
					"Expected to be able to draw assertion subject from GenSource, but failed with:",
					"    " + cause
				));
	}

}
