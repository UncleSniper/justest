package org.unclesniper.test.matcher;

import java.util.function.Consumer;
import org.unclesniper.test.IndentSink;

import static org.unclesniper.test.TestUtils.notNull;

public class TypeExceptionMatcher<InT extends Throwable, OutT extends Throwable>
		implements ExceptionMatcher<InT, OutT> {

	private final Class<OutT> exceptionClass;

	public TypeExceptionMatcher(Class<OutT> exceptionClass) {
		this.exceptionClass = notNull(exceptionClass, "Exception class");
	}

	public Class<OutT> getExceptionClass() {
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

	@Override
	public void describe(IndentSink sink) {
		notNull(sink, "Sink");
		sink.append("to be of type " + exceptionClass.getName(), false);
	}

}
