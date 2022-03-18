package org.unclesniper.test;

import java.util.Collection;
import java.util.stream.Stream;
import java.util.function.Supplier;

public class AssertionFailureError extends AbstractFailureError {

	public AssertionFailureError(String message, Supplier<? extends Stream<String>> description) {
		super(message, description);
	}

	public AssertionFailureError(String message, Supplier<? extends Stream<String>> description, Throwable cause) {
		super(message, description, cause);
	}

	public AssertionFailureError(String message, Collection<String> description) {
		super(message, description);
	}

	public AssertionFailureError(String message, Collection<String> description, Throwable cause) {
		super(message, description, cause);
	}

}
