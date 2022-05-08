package org.unclesniper.test.matcher;

public class CompareAssertionFailureError extends AbstractInfoAssertionFailureError {

	public CompareAssertionFailureError(CompareInfo info) {
		super(info);
	}

	public CompareAssertionFailureError(CompareInfo info, Throwable cause) {
		super(info, cause);
	}

	@Override
	public CompareInfo getInfo() {
		return (CompareInfo)super.getInfo();
	}

}
