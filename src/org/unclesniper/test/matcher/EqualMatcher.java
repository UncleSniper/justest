package org.unclesniper.test.matcher;

import java.util.Objects;

public class EqualMatcher<SubjectT> implements Matcher<SubjectT, SubjectT> {

	private final SubjectT expected;

	private final boolean negated;

	public EqualMatcher(SubjectT expected, boolean negated) {
		this.expected = expected;
		this.negated = negated;
	}

	public SubjectT getExpected() {
		return expected;
	}

	public boolean isNegated() {
		return negated;
	}

	@Override
	public SubjectT match(SubjectT actual, boolean assume) {
		if(Objects.equals(actual, expected) != negated)
			return actual;
		EqualInfo info = new EqualInfo(expected, actual, negated);
		if(assume)
			throw new EqualAssumptionFailureError(info);
		else
			throw new EqualAssertionFailureError(info);
	}

}
