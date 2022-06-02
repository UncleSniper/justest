package org.unclesniper.test.matcher;

import static org.unclesniper.test.TestUtils.notNull;

public class NumericallyCloseInfo extends AbstractInfo {

	private final Number expected;

	private final Number actual;

	private final double epsilon;

	private final boolean negated;

	public NumericallyCloseInfo(Number expected, Number actual, double epsilon, boolean negated) {
		this.expected = notNull(expected, "Expected number");
		this.actual = actual;
		this.epsilon = epsilon;
		this.negated = negated;
	}

	public Number getExpected() {
		return expected;
	}

	public Number getActual() {
		return actual;
	}

	public double getEpsilon() {
		return epsilon;
	}

	public boolean isNegated() {
		return negated;
	}

	@Override
	protected void make(Sink sink) {
		sink.append("Expected", false);
		sink.append(actual == null ? "<null>" : actual.toString(), true);
		sink.append("to be within", false);
		sink.append(String.valueOf(epsilon), true);
		sink.append("of", false);
		sink.append(expected.toString(), true);
		sink.append(actual == null ? "but was null" : "but was not", false);
	}

}
