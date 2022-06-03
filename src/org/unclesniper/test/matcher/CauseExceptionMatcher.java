package org.unclesniper.test.matcher;

import java.util.function.Consumer;
import org.unclesniper.test.IndentSink;

import static org.unclesniper.test.TestUtils.notNull;

public class CauseExceptionMatcher<SubjectT extends Throwable> implements ExceptionMatcher<SubjectT, SubjectT> {

	private final ExceptionMatcher<? extends Throwable, ? extends Throwable> causeMatcher;

	public CauseExceptionMatcher(ExceptionMatcher<? extends Throwable, ? extends Throwable> causeMatcher) {
		this.causeMatcher = causeMatcher;
	}

	public ExceptionMatcher<? extends Throwable, ? extends Throwable> getCauseMatcher() {
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

	@Override
	public void describe(IndentSink sink) {
		notNull(sink, "Sink");
		if(causeMatcher == null) {
			sink.append("not to have a cause", false);
			return;
		}
		sink.append("to have a cause, and that cause", false);
		causeMatcher.describe(sink);
	}

}
