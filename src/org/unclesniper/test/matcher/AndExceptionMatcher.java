package org.unclesniper.test.matcher;

import java.util.List;
import java.util.LinkedList;
import java.util.function.Consumer;

public class AndExceptionMatcher implements ExceptionMatcher {

	private final List<ExceptionMatcher> matchers = new LinkedList<ExceptionMatcher>();

	public AndExceptionMatcher() {}

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
			if(!matcher.isExpectedException(exception))
				return false;
		}
		return true;
	}

	@Override
	public void describeExpectedException(Consumer<String> sink) {
		for(ExceptionMatcher matcher : matchers)
			matcher.describeExpectedException(sink);
	}

}
