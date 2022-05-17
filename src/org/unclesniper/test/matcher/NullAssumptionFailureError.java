package org.unclesniper.test.matcher;

public class NullAssumptionFailureError extends AbstractInfoAssumptionFailureError {

	public NullAssumptionFailureError(NullInfo info) {
		super(info);
	}

	@Override
	public NullInfo getInfo() {
		return (NullInfo)super.getInfo();
	}

}
