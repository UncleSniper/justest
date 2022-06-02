package org.unclesniper.test.matcher;

import java.util.List;
import java.util.LinkedList;
import java.util.function.Consumer;

public class AndExceptionMatcher<SubjectT extends Throwable> implements ExceptionMatcher<SubjectT, SubjectT> {

	private final List<ExceptionMatcher<? extends Throwable, ? extends Throwable>> matchers
			= new LinkedList<ExceptionMatcher<? extends Throwable, ? extends Throwable>>();

	public AndExceptionMatcher() {}

	public void addMatcher(ExceptionMatcher<? extends Throwable, ? extends Throwable> matcher) {
		if(matcher != null)
			matchers.add(matcher);
	}

	public boolean isEmpty() {
		return matchers.isEmpty();
	}

	@Override
	public boolean isExpectedException(Throwable exception) {
		for(ExceptionMatcher<? extends Throwable, ? extends Throwable> matcher : matchers) {
			if(!matcher.isExpectedException(exception))
				return false;
		}
		return true;
	}

	@Override
	public void describeExpectedException(Consumer<String> sink) {
		for(ExceptionMatcher<? extends Throwable, ? extends Throwable> matcher : matchers)
			matcher.describeExpectedException(sink);
	}

}
