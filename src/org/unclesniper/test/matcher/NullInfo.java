package org.unclesniper.test.matcher;

import org.unclesniper.test.TestUtils;

public class NullInfo extends AbstractInfo {

	private final Object actual;

	private final boolean negated;

	public NullInfo(Object actual, boolean negated) {
		this.actual = actual;
		this.negated = negated;
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
		sink.append(negated ? "not to be null, but was" : "to be null, but was not", false);
	}

}
