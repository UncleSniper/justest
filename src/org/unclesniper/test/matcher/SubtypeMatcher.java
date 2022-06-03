package org.unclesniper.test.matcher;

import org.unclesniper.test.IndentSink;

import static org.unclesniper.test.TestUtils.notNull;

public class SubtypeMatcher<InT, OutT> implements Matcher<InT, OutT> {

	private final Class<? extends OutT> expectedType;

	public SubtypeMatcher(Class<? extends OutT> expectedType) {
		this.expectedType = notNull(expectedType, "Expected type");
	}

	public Class<? extends OutT> getExpectedType() {
		return expectedType;
	}

	@Override
	public OutT match(InT actual, boolean assume) {
		if(expectedType.isInstance(actual))
			return expectedType.cast(actual);
		TypeInfo info = new TypeInfo(expectedType, actual, false);
		if(assume)
			throw new TypeAssumptionFailureError(info);
		else
			throw new TypeAssertionFailureError(info);
	}

	@Override
	public boolean matches(InT actual) {
		return expectedType.isInstance(actual);
	}

	@Override
	public void describe(IndentSink sink) {
		notNull(sink, "Sink");
		sink.append("to be of type", false);
		sink.append(expectedType.getName(), true);
	}

}
