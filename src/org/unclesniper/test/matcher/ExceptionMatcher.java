package org.unclesniper.test.matcher;

import java.util.function.Consumer;

public interface ExceptionMatcher<InT extends Throwable, OutT extends Throwable> extends Matcher<InT, OutT> {

	boolean isExpectedException(Throwable exception);

	void describeExpectedException(Consumer<String> sink);

	@Override
	@SuppressWarnings("unchecked")
	default OutT match(InT subject, boolean assume) {
		if(subject != null && this.isExpectedException(subject))
			return (OutT)subject;
		ExceptionInfo info = new ExceptionInfo(this, subject);
		if(assume)
			throw new ExceptionAssumptionFailureError(info);
		else
			throw new ExceptionAssertionFailureError(info);
	}

	@Override
	default boolean matches(InT subject) {
		return isExpectedException(subject);
	}

}
