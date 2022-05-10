package org.unclesniper.test.matcher;

import java.util.List;
import java.util.LinkedList;
import java.util.function.Consumer;

import static org.unclesniper.test.TestUtils.notNull;

public class OrExceptionMatcher implements ExceptionMatcher {

	private final List<ExceptionMatcher> matchers = new LinkedList<ExceptionMatcher>();

	public OrExceptionMatcher() {}

	public void addMatcher(ExceptionMatcher matcher) {
		if(matcher != null)
			matchers.add(matcher);
	}

	public boolean isEmpty() {
		return matchers.isEmpty();
	}

	@Override
	public boolean isExpectedException(Throwable exception) {
		for(ExceptionMatcher matcher : matchers) {
			if(matcher.isExpectedException(exception))
				return true;
		}
		return false;
	}

	@Override
	public void describeExpectedException(Consumer<String> sink) {
		notNull(sink, "Sink").accept("- any of:");
		ExceptionMatcherSubSink subSink = new ExceptionMatcherSubSink(line -> {
			if(line != null)
				sink.accept("  " + line);
		});
		for(ExceptionMatcher matcher : matchers) {
			subSink.reset();
			matcher.describeExpectedException(subSink);
		}
	}

}
