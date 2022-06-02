package org.unclesniper.test.matcher;

import org.unclesniper.test.Executable;
import org.unclesniper.test.AssertionFailureError;
import org.unclesniper.test.AssumptionFailureError;

import static org.unclesniper.test.TestUtils.notNull;

public class ThrowsYieldMatcher<OutT extends Throwable> implements Matcher<Executable, OutT> {

	private final ExceptionMatcher<? extends Throwable, ? extends OutT> expectedException;

	public ThrowsYieldMatcher(ExceptionMatcher<? extends Throwable, ? extends OutT> expectedException) {
		this.expectedException = notNull(expectedException, "Expected exception matcher");
	}

	public ExceptionMatcher<? extends Throwable, ? extends OutT> getExpectedException() {
		return expectedException;
	}

	@Override
	@SuppressWarnings("unchecked")
	public OutT match(Executable subject, boolean assume) {
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
			if(thrownException != null && expectedException.isExpectedException(thrownException))
				return (OutT)thrownException;
		}
		ThrowsInfo info = new ThrowsInfo(subject, expectedException, thrownException);
		if(assume)
			throw new ThrowsAssumptionFailureError(info);
		else
			throw new ThrowsAssertionFailureError(info);
	}

}
