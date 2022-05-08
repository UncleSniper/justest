package org.unclesniper.test.matcher;

public class CompareAssertionFailureError extends AbstractInfoAssertionFailureError {

	public CompareAssertionFailureError(CompareInfo info) {
		super(info);
	}

	@Override
	public CompareInfo getInfo() {
		return (CompareInfo)super.getInfo();
	}

}
