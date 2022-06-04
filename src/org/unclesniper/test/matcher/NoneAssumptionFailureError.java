package org.unclesniper.test.matcher;

public class NoneAssumptionFailureError extends AbstractInfoAssumptionFailureError {

	public NoneAssumptionFailureError(NoneInfo info) {
		super(info);
	}

	@Override
	public NoneInfo getInfo() {
		return (NoneInfo)super.getInfo();
	}

}
