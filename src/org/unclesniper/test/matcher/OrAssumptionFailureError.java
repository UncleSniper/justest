package org.unclesniper.test.matcher;

public class OrAssumptionFailureError extends AbstractInfoAssumptionFailureError {

	public OrAssumptionFailureError(OrInfo info) {
		super(info);
	}

	@Override
	public OrInfo getInfo() {
		return (OrInfo)super.getInfo();
	}

}
