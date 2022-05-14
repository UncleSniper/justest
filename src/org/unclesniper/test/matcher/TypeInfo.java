package org.unclesniper.test.matcher;

import org.unclesniper.test.TestUtils;

import static org.unclesniper.test.TestUtils.notNull;

public class TypeInfo extends AbstractInfo {

	private final Class<?> expectedType;

	private final Object actual;

	private final boolean negated;

	public TypeInfo(Class<?> expectedType, Object actual) {
		this(expectedType, actual, false);
	}

	public TypeInfo(Class<?> expectedType, Object actual, boolean negated) {
		this.expectedType = notNull(expectedType, "Expected type");
		this.actual = actual;
		this.negated = negated;
	}

	public Class<?> getExpectedType() {
		return expectedType;
	}

	public Object getActual() {
		return actual;
	}

	public boolean isNegated() {
		return negated;
	}

	@Override
	protected void make(Sink sink) {
		sink.append("Expected", false);
		sink.append(TestUtils.describeObject(actual), true);
		sink.append(negated ? "not to be of type" : "to be of type", false);
		sink.append(expectedType.getName(), true);
		sink.append(actual == null ? "but was null" : "but was not", false);
	}

}
