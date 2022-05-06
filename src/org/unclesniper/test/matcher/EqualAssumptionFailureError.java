package org.unclesniper.test.matcher;

public class EqualAssumptionFailureError extends AbstractInfoAssumptionFailureError {

	public EqualAssumptionFailureError(EqualInfo info) {
		super(info);
	}

	@Override
	public EqualInfo getInfo() {
		return (EqualInfo)super.getInfo();
	}

}
