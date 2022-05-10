package org.unclesniper.test.matcher;

import java.util.function.Consumer;

import static org.unclesniper.test.TestUtils.notNull;

public class CauseExceptionMatcher implements ExceptionMatcher {

	private final ExceptionMatcher causeMatcher;

	public CauseExceptionMatcher(ExceptionMatcher causeMatcher) {
		this.causeMatcher = causeMatcher;
	}

	public ExceptionMatcher getCauseMatcher() {
		return causeMatcher;
	}

	@Override
	public boolean isExpectedException(Throwable exception) {
		if(exception == null)
			return false;
		Throwable cause = exception.getCause();
		if(causeMatcher == null)
			return cause == null;
		if(cause == null)
			return false;
		return causeMatcher.isExpectedException(cause);
	}

	@Override
	public void describeExpectedException(Consumer<String> sink) {
		notNull(sink, "Sink");
		if(causeMatcher == null) {
			sink.accept("- without cause");
			return;
		}
		sink.accept("- caused by exception");
		causeMatcher.describeExpectedException(line -> {
			if(line == null)
				return;
			sink.accept("  " + line);
		});
	}

}
