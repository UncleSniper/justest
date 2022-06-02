package org.unclesniper.test.matcher;

public class NumericallyCloseAssertionFailureError extends AbstractInfoAssertionFailureError {

	public NumericallyCloseAssertionFailureError(NumericallyCloseInfo info) {
		super(info);
	}

	@Override
	public NumericallyCloseInfo getInfo() {
		return (NumericallyCloseInfo)super.getInfo();
	}

}
