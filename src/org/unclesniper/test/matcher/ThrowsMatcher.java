package org.unclesniper.test.matcher;

import org.unclesniper.test.Executable;
import org.unclesniper.test.IndentSink;
import org.unclesniper.test.AssertionFailureError;
import org.unclesniper.test.AssumptionFailureError;

import static org.unclesniper.test.TestUtils.notNull;

public class ThrowsMatcher implements Matcher<Executable, Executable> {

	private final ExceptionMatcher<? extends Throwable, ? extends Throwable> expectedException;

	public ThrowsMatcher(ExceptionMatcher<? extends Throwable, ? extends Throwable> expectedException) {
		this.expectedException = expectedException;
	}

	public ExceptionMatcher<? extends Throwable, ? extends Throwable> getExpectedException() {
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

	@Override
	public boolean matches(Executable subject) {
		if(subject == null)
			return false;
		Throwable thrownException = null;
		try {
			subject.execute();
		}
		catch(AssertionFailureError | AssumptionFailureError e) {
			throw e;
		}
		catch(Throwable t) {
			thrownException = t;
		}
		if(expectedException == null)
			return thrownException == null;
		return thrownException != null && expectedException.isExpectedException(thrownException);
	}

	@Override
	public void describe(IndentSink sink) {
		notNull(sink, "Sink");
		if(expectedException == null)
			sink.append("not to throw any exception", false);
		else {
			sink.append("to throw an exception", false);
			expectedException.describeExpectedException(line -> {
				if(line != null)
					sink.append(line, true);
			});
		}
	}

}
