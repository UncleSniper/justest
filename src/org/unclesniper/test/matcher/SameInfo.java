package org.unclesniper.test.matcher;

import org.unclesniper.test.TestUtils;

public class SameInfo extends AbstractInfo {

	private final Object expected;

	private final Object actual;

	private final boolean negated;

	public SameInfo(Object expected, Object actual, boolean negated) {
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
		if(negated) {
			sink.append("not to be the same object as... well, itself", false);
			sink.append("but was", false);
		}
		else {
			sink.append("to be the same object as", false);
			sink.append(TestUtils.describeObject(expected), true);
			sink.append("but was not", false);
		}
	}

}
