package org.unclesniper.test;

import java.util.stream.Stream;

public class UnconditionalAssertionFailureError extends AssertionFailureError {

	public UnconditionalAssertionFailureError(String message) {
		super(message, () -> Stream.of(message));
		if(message == null)
			throw new IllegalArgumentException("Message must not be null");
	}

}
