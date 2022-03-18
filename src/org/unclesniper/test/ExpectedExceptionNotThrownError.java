package org.unclesniper.test;

import java.util.stream.Stream;
import java.util.function.Predicate;

public class ExpectedExceptionNotThrownError extends AssertionFailureError {

	private final Predicate<? super Throwable> expectedException;

	private final String expectedExceptionName;

	public ExpectedExceptionNotThrownError(Predicate<? super Throwable> expectedException,
			String expectedExceptionName) {
		super("Expected " + (expectedExceptionName == null ? "exception" : expectedExceptionName)
				+ " to be thrown, but nothing was actually thrown",
				() -> ExpectedExceptionNotThrownError.makeDesciption(expectedExceptionName));
		this.expectedException = expectedException;
		this.expectedExceptionName = expectedExceptionName;
	}

	public Predicate<? super Throwable> getExpectedException() {
		return expectedException;
	}

	public String getExpectedExceptionName() {
		return expectedExceptionName;
	}

	private static Stream<String> makeDesciption(String expectedExceptionName) {
		if(expectedExceptionName == null)
			return Stream.of("Expected exception to be thrown, but nothing was actually thrown");
		return Stream.of(
			"Expected",
			"    " + expectedExceptionName,
			"to be thrown, but nothing was actually thrown"
		);
	}

}
