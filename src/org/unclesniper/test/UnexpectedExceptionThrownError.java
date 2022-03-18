package org.unclesniper.test;

import java.util.stream.Stream;

public class UnexpectedExceptionThrownError extends AssertionFailureError {

	public UnexpectedExceptionThrownError(Throwable cause) {
		super("Expected no exception to be thrown, but " + (cause == null ? "one" : cause.getClass().getName())
				+ " was thrown", () -> UnexpectedExceptionThrownError.makeDesciption(cause), cause);
	}

	private static Stream<String> makeDesciption(Throwable cause) {
		if(cause == null)
			return Stream.of("Expected no exception to be thrown, but one was thrown");
		return Stream.of(
			"Expected no exception to be thrown, but an instance of",
			"    " + cause.getClass().getName(),
			"namely",
			"    " + cause,
			"was thrown"
		);
	}

}
