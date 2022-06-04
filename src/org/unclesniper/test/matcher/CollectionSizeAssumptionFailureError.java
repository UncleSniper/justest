package org.unclesniper.test.matcher;

public class CollectionSizeAssumptionFailureError extends AbstractInfoAssumptionFailureError {

	public CollectionSizeAssumptionFailureError(CollectionSizeInfo info) {
		super(info);
	}

	@Override
	public CollectionSizeInfo getInfo() {
		return (CollectionSizeInfo)super.getInfo();
	}

}
