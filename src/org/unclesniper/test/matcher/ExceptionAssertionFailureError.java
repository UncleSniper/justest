package org.unclesniper.test.matcher;

public class ExceptionAssertionFailureError extends AbstractInfoAssertionFailureError {

	public ExceptionAssertionFailureError(ExceptionInfo info) {
		super(info);
	}

	@Override
	public ExceptionInfo getInfo() {
		return (ExceptionInfo)super.getInfo();
	}

}
