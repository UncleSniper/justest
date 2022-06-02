package org.unclesniper.test.matcher;

public class SameAssumptionFailureError extends AbstractInfoAssumptionFailureError {

	public SameAssumptionFailureError(SameInfo info) {
		super(info);
	}

	@Override
	public SameInfo getInfo() {
		return (SameInfo)super.getInfo();
	}

}
