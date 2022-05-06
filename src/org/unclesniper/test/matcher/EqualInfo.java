package org.unclesniper.test.matcher;

import org.unclesniper.test.TestUtils;

public class EqualInfo extends AbstractInfo {

	private final Object expected;

	private final Object actual;

	private final boolean negated;

	public EqualInfo(Object expected, Object actual, boolean negated) {
		this.expected = expected;
		this.actual = actual;
		this.negated = negated;
	}

	public Object getExpected() {
		return expected;
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
		sink.append(negated ? "to be unequal to" : "to be equal to", false);
		sink.append(TestUtils.describeObject(expected), true);
		sink.append("but was not", false);
	}

}
