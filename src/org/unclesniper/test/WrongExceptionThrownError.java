package org.unclesniper.test;

import java.util.stream.Stream;
import java.util.function.Predicate;

public class WrongExceptionThrownError extends AssertionFailureError {

	private final Predicate<? super Throwable> expectedException;

	private final String expectedExceptionName;

	public WrongExceptionThrownError(Predicate<? super Throwable> expectedException,
			String expectedExceptionName, Throwable cause) {
		super("Expected " + (expectedExceptionName == null ? "exception" : expectedExceptionName)
				+ " to be thrown, but " + (cause == null ? "a different exception" : cause.getClass().getName())
				+ " was actually thrown",
				() -> WrongExceptionThrownError.makeDesciption(expectedExceptionName, cause), cause);
		this.expectedException = expectedException;
		this.expectedExceptionName = expectedExceptionName;
	}

	public Predicate<? super Throwable> getExpectedException() {
		return expectedException;
	}

	public String getExpectedExceptionName() {
		return expectedExceptionName;
	}

	private static Stream<String> makeDesciption(String expectedExceptionName, Throwable cause) {
		if(expectedExceptionName == null) {
			if(cause == null)
				return Stream.of("Expected exception to be thrown, but a different exception was actually thrown");
			return Stream.of(
				"Expected exception to be thrown, but an instance of",
				"    " + cause.getClass().getName(),
				"namely",
				"    " + cause,
				"was actually thrown"
			);
		}
		if(cause == null)
			return Stream.of(
				"Expected",
				"    " + expectedExceptionName,
				"to be thrown, but a different exception was actually thrown"
			);
		return Stream.of(
			"Expected",
			"    " + expectedExceptionName,
			"to be thrown, but an instance of",
			"    " + cause.getClass().getName(),
			"namely",
			"    " + cause,
			"was actually thrown"
		);
	}

}
