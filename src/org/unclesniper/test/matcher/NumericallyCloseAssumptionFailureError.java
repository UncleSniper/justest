package org.unclesniper.test.matcher;

public class NumericallyCloseAssumptionFailureError extends AbstractInfoAssumptionFailureError {

	public NumericallyCloseAssumptionFailureError(NumericallyCloseInfo info) {
		super(info);
	}

	@Override
	public NumericallyCloseInfo getInfo() {
		return (NumericallyCloseInfo)super.getInfo();
	}

}
