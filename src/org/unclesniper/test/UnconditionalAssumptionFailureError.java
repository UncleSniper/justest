package org.unclesniper.test;

import java.util.stream.Stream;

public class UnconditionalAssumptionFailureError extends AssumptionFailureError {

	public UnconditionalAssumptionFailureError(String message) {
		super(message, () -> Stream.of(message));
		if(message == null)
			throw new IllegalArgumentException("Message must not be null");
	}

}
