package org.unclesniper.test.matcher;

public class ThrowsAssertionFailureError extends AbstractInfoAssertionFailureError {

	public ThrowsAssertionFailureError(ThrowsInfo info) {
		super(info);
	}

	@Override
	public ThrowsInfo getInfo() {
		return (ThrowsInfo)super.getInfo();
	}

}
