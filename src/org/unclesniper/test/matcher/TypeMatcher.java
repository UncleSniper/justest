package org.unclesniper.test.matcher;

import org.unclesniper.test.IndentSink;

import static org.unclesniper.test.TestUtils.notNull;

public class TypeMatcher<SubjectT> implements Matcher<SubjectT, SubjectT> {

	private final Class<?> expectedType;

	private final boolean negated;

	public TypeMatcher(Class<?> expectedType) {
		this(expectedType, false);
	}

	public TypeMatcher(Class<?> expectedType, boolean negated) {
		this.expectedType = notNull(expectedType, "Expected type");
		this.negated = negated;
	}

	public Class<?> getExpectedType() {
		return expectedType;
	}

	public boolean isNegated() {
		return negated;
	}

	@Override
	public SubjectT match(SubjectT actual, boolean assume) {
		if(expectedType.isInstance(actual) != negated)
			return actual;
		TypeInfo info = new TypeInfo(expectedType, actual, negated);
		if(assume)
			throw new TypeAssumptionFailureError(info);
		else
			throw new TypeAssertionFailureError(info);
	}

	@Override
	public boolean matches(SubjectT actual) {
		return expectedType.isInstance(actual);
	}

	@Override
	public void describe(IndentSink sink) {
		notNull(sink, "Sink");
		sink.append(negated ? "not to be of type" : "to be of type", false);
		sink.append(expectedType.getName(), true);
	}

}
