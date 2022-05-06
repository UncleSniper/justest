package org.unclesniper.test;

import java.util.stream.Stream;

import static org.unclesniper.test.TestUtils.notNull;

public class UnconditionalAssumptionFailureError extends AssumptionFailureError {

	public UnconditionalAssumptionFailureError(String message) {
		super(notNull(message, "Message"), () -> Stream.of(message));
	}

}
