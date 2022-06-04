package org.unclesniper.test.matcher;

public class NoneAssertionFailureError extends AbstractInfoAssertionFailureError {

	public NoneAssertionFailureError(NoneInfo info) {
		super(info);
	}

	@Override
	public NoneInfo getInfo() {
		return (NoneInfo)super.getInfo();
	}

}
