package org.unclesniper.test.matcher;

import org.unclesniper.test.IndentSink;
import java.util.function.ToIntFunction;

import static org.unclesniper.test.TestUtils.notNull;

public class CollectionSizeMatcher<CollectionT> implements Matcher<CollectionT, CollectionT> {

	private final ToIntFunction<? super CollectionT> sizeFunction;

	private final Matcher<? super Integer, ?> sizeMatcher;

	public CollectionSizeMatcher(ToIntFunction<? super CollectionT> sizeFunction,
			Matcher<? super Integer, ?> sizeMatcher) {
		this.sizeFunction = notNull(sizeFunction, "Size function");
		this.sizeMatcher = notNull(sizeMatcher, "Size matcher");
	}

	public ToIntFunction<? super CollectionT> getSizeFunction() {
		return sizeFunction;
	}

	public Matcher<? super Integer, ?> getSizeMatcher() {
		return sizeMatcher;
	}

	@Override
	public CollectionT match(CollectionT collection, boolean assume) {
		int actualSize = 0;
		if(collection != null) {
			actualSize = sizeFunction.applyAsInt(collection);
			if(sizeMatcher.matches(actualSize))
				return collection;
		}
		CollectionSizeInfo info = new CollectionSizeInfo(collection, sizeMatcher, actualSize);
		if(assume)
			throw new CollectionSizeAssumptionFailureError(info);
		else
			throw new CollectionSizeAssertionFailureError(info);
	}

	@Override
	public boolean matches(CollectionT actual) {
		if(actual == null)
			return false;
		return sizeMatcher.matches(sizeFunction.applyAsInt(actual));
	}

	@Override
	public void describe(IndentSink sink) {
		notNull(sink, "Sink");
		sink.append("to have a size, and that size", false);
		sizeMatcher.describe(sink);
	}

}
