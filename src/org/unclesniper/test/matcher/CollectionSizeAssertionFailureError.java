package org.unclesniper.test.matcher;

public class CollectionSizeAssertionFailureError extends AbstractInfoAssertionFailureError {

	public CollectionSizeAssertionFailureError(CollectionSizeInfo info) {
		super(info);
	}

	@Override
	public CollectionSizeInfo getInfo() {
		return (CollectionSizeInfo)super.getInfo();
	}

}
