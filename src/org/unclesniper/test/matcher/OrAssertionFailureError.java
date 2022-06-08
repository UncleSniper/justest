package org.unclesniper.test.matcher;

public class OrAssertionFailureError extends AbstractInfoAssertionFailureError {

	public OrAssertionFailureError(OrInfo info) {
		super(info);
	}

	@Override
	public OrInfo getInfo() {
		return (OrInfo)super.getInfo();
	}

}
