package org.unclesniper.test.matcher;

public class CompareAssumptionFailureError extends AbstractInfoAssumptionFailureError {

	public CompareAssumptionFailureError(CompareInfo info) {
		super(info);
	}

	@Override
	public CompareInfo getInfo() {
		return (CompareInfo)super.getInfo();
	}

}
