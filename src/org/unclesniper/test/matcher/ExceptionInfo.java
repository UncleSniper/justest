package org.unclesniper.test.matcher;

import org.unclesniper.test.TestUtils;

import static org.unclesniper.test.TestUtils.notNull;

public class ExceptionInfo extends AbstractInfo {

	private final ExceptionMatcher<? extends Throwable, ? extends Throwable> expectedException;

	private final Throwable actualException;

	public ExceptionInfo(ExceptionMatcher<? extends Throwable, ? extends Throwable> expectedException,
			Throwable actualException) {
		this.expectedException = notNull(expectedException, "Exception matcher");
		this.actualException = actualException;
	}

	public ExceptionMatcher<? extends Throwable, ? extends Throwable> getExpectedException() {
		return expectedException;
	}

	public Throwable getActualException() {
		return actualException;
	}

	@Override
	protected void make(Sink sink) {
		sink.append("Expected", false);
		sink.append(TestUtils.describeObject(actualException), true);
		sink.append("to be an exception", false);
		expectedException.describeExpectedException(line -> {
			if(line != null)
				sink.append(line, true);
		});
		sink.append(actualException == null ? "but was null" : "but was not", false);
	}

}
