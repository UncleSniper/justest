package org.unclesniper.test.matcher;

import org.unclesniper.test.Executable;
import org.unclesniper.test.AssertionFailureError;
import org.unclesniper.test.AssumptionFailureError;

public class ThrowsMatcher implements Matcher<Executable, Executable> {

	private final ExceptionMatcher expectedException;

	public ThrowsMatcher(ExceptionMatcher expectedException) {
		this.expectedException = expectedException;
	}

	public ExceptionMatcher getExpectedException() {
		return expectedException;
	}

	@Override
	public Executable match(Executable subject, boolean assume) {
		Throwable thrownException = null;
		if(subject != null) {
			try {
				subject.execute();
			}
			catch(AssertionFailureError | AssumptionFailureError e) {
				throw e;
			}
			catch(Throwable t) {
				thrownException = t;
			}
			if(expectedException == null) {
				if(thrownException == null)
					return subject;
			}
			else if(thrownException != null && expectedException.isExpectedException(thrownException))
				return subject;
		}
		ThrowsInfo info = new ThrowsInfo(subject, expectedException, thrownException);
		if(assume)
			throw new ThrowsAssumptionFailureError(info);
		else
			throw new ThrowsAssertionFailureError(info);
	}

}
