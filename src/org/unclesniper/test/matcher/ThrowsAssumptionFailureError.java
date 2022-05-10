package org.unclesniper.test.matcher;

public class ThrowsAssumptionFailureError extends AbstractInfoAssumptionFailureError {

	public ThrowsAssumptionFailureError(ThrowsInfo info) {
		super(info);
	}

	@Override
	public ThrowsInfo getInfo() {
		return (ThrowsInfo)super.getInfo();
	}

}
