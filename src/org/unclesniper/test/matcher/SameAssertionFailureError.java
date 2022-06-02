package org.unclesniper.test.matcher;

public class SameAssertionFailureError extends AbstractInfoAssertionFailureError {

	public SameAssertionFailureError(SameInfo info) {
		super(info);
	}

	@Override
	public SameInfo getInfo() {
		return (SameInfo)super.getInfo();
	}

}
