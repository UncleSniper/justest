package org.unclesniper.test.matcher;

import java.util.function.Consumer;

import static org.unclesniper.test.TestUtils.notNull;

public class TypeExceptionMatcher implements ExceptionMatcher {

	private final Class<? extends Throwable> exceptionClass;

	public TypeExceptionMatcher(Class<? extends Throwable> exceptionClass) {
		this.exceptionClass = notNull(exceptionClass, "Exception class");
	}

	public Class<? extends Throwable> getExceptionClass() {
		return exceptionClass;
	}

	@Override
	public boolean isExpectedException(Throwable exception) {
		return exceptionClass.isInstance(exception);
	}

	@Override
	public void describeExpectedException(Consumer<String> sink) {
		notNull(sink, "Sink").accept("- of type " + exceptionClass.getName());
	}

}
