package org.unclesniper.test.matcher;

import org.unclesniper.test.TestUtils;
import org.unclesniper.test.Executable;

public class ThrowsInfo extends AbstractInfo {

	private final Executable executable;

	private final ExceptionMatcher<? extends Throwable, ? extends Throwable> expectedException;

	private final Throwable thrownException;

	public ThrowsInfo(Executable executable,
			ExceptionMatcher<? extends Throwable, ? extends Throwable> expectedException,
			Throwable thrownException) {
		this.executable = executable;
		this.expectedException = expectedException;
		this.thrownException = thrownException;
	}

	public Executable getExecutable() {
		return executable;
	}

	public ExceptionMatcher<? extends Throwable, ? extends Throwable> getExpectedException() {
		return expectedException;
	}

	public Throwable getThrownException() {
		return thrownException;
	}

	@Override
	protected void make(Sink sink) {
		sink.append("Expected executable", false);
		sink.append(TestUtils.describeObject(executable), true);
		if(expectedException == null)
			sink.append("not to throw any exception", false);
		else {
			sink.append("to throw an exception", false);
			expectedException.describeExpectedException(line -> {
				if(line != null)
					sink.append(line, true);
			});
		}
		if(executable == null)
			sink.append("but executable was null", false);
		else if(thrownException == null)
			sink.append("but did not throw any exception", false);
		else {
			sink.append("but threw", false);
			sink.append(TestUtils.describeObject(thrownException), true);
		}
	}

}
