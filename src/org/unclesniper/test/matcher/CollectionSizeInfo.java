package org.unclesniper.test.matcher;

import org.unclesniper.test.TestUtils;
import org.unclesniper.test.IndentSink;

public class CollectionSizeInfo extends AbstractInfo {

	private final Object collection;

	private final Matcher<? super Integer, ?> expectedSize;

	private final int actualSize;

	public CollectionSizeInfo(Object collection, Matcher<? super Integer, ?> expectedSize, int actualSize) {
		this.collection = collection;
		this.expectedSize = expectedSize;
		this.actualSize = actualSize;
	}

	public Object getCollection() {
		return collection;
	}

	public Matcher<? super Integer, ?> getExpectedSize() {
		return expectedSize;
	}

	public int getActualSize() {
		return actualSize;
	}

	@Override
	protected void make(IndentSink sink) {
		sink.append("Expected", false);
		sink.append(TestUtils.describeObject(collection), true);
		sink.append("to have a size, and that size", false);
		expectedSize.describe(sink);
		if(collection == null)
			sink.append("but collection was null", false);
		else {
			sink.append("but size was", false);
			sink.append(String.valueOf(actualSize), true);
		}
	}

}
