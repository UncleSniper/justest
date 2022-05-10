package org.unclesniper.test.matcher;

import java.util.function.Consumer;

public interface ExceptionMatcher {

	boolean isExpectedException(Throwable exception);

	void describeExpectedException(Consumer<String> sink);

}
