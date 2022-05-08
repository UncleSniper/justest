package org.unclesniper.test.matcher;

public class CompareAssumptionFailureError extends AbstractInfoAssumptionFailureError {

	public CompareAssumptionFailureError(CompareInfo info) {
		super(info);
	}

	public CompareAssumptionFailureError(CompareInfo info, Throwable cause) {
		super(info, cause);
	}

	@Override
	public CompareInfo getInfo() {
		return (CompareInfo)super.getInfo();
	}

}
