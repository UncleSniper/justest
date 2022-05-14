package org.unclesniper.test.matcher;

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

}
