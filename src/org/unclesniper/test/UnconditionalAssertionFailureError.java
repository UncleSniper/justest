package org.unclesniper.test;

import java.util.stream.Stream;

import static org.unclesniper.test.TestUtils.notNull;

public class UnconditionalAssertionFailureError extends AssertionFailureError {

	public UnconditionalAssertionFailureError(String message) {
		super(notNull(message, "Message"), () -> Stream.of(message));
	}

}
