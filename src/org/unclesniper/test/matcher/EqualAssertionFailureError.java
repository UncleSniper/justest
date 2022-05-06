package org.unclesniper.test.matcher;

public class EqualAssertionFailureError extends AbstractInfoAssertionFailureError {

	public EqualAssertionFailureError(EqualInfo info) {
		super(info);
	}

	@Override
	public EqualInfo getInfo() {
		return (EqualInfo)super.getInfo();
	}

}
