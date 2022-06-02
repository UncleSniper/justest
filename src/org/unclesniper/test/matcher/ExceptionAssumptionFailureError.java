package org.unclesniper.test.matcher;

public class ExceptionAssumptionFailureError extends AbstractInfoAssumptionFailureError {

	public ExceptionAssumptionFailureError(ExceptionInfo info) {
		super(info);
	}

	@Override
	public ExceptionInfo getInfo() {
		return (ExceptionInfo)super.getInfo();
	}

}
