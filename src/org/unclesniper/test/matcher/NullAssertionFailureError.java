package org.unclesniper.test.matcher;

public class NullAssertionFailureError extends AbstractInfoAssertionFailureError {

	public NullAssertionFailureError(NullInfo info) {
		super(info);
	}

	@Override
	public NullInfo getInfo() {
		return (NullInfo)super.getInfo();
	}

}
