package org.unclesniper.test.matcher;

import static org.unclesniper.test.TestUtils.notNull;

public class NumericallyCloseMatcher<SubjectT extends Number> implements Matcher<SubjectT, SubjectT> {

	private final Number expected;

	private final double epsilon;

	private final boolean negated;

	public NumericallyCloseMatcher(Number expected, double epsilon) {
		this(expected, epsilon, false);
	}

	public NumericallyCloseMatcher(Number expected, double epsilon, boolean negated) {
		this.expected = notNull(expected, "Expected number");
		this.epsilon = epsilon;
		this.negated = negated;
	}

	public Number getExpected() {
		return expected;
	}

	public double getEpsilon() {
		return epsilon;
	}

	public boolean isNegated() {
		return negated;
	}

	@Override
	public SubjectT match(SubjectT actual, boolean assume) {
		double ex = expected.doubleValue();
		double ac = actual == null ? 0.0 : actual.doubleValue();
		double eps = Math.abs(epsilon);
		if(actual != null && (Math.abs(ex - ac) <= eps) != negated)
			return actual;
		NumericallyCloseInfo info = new NumericallyCloseInfo(expected, actual, epsilon, negated);
		if(assume)
			throw new NumericallyCloseAssumptionFailureError(info);
		else
			throw new NumericallyCloseAssertionFailureError(info);
	}

}
