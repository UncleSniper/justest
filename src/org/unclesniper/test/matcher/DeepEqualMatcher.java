package org.unclesniper.test.matcher;

import org.unclesniper.test.deepeq.DeepEquals;
import org.unclesniper.test.deepeq.DeepCompareConfig;

public class DeepEqualMatcher<SubjectT> implements DeepEqualConfigurer<SubjectT> {

	private final SubjectT expected;

	private final boolean negated;

	private DeepCompareConfig config;

	public DeepEqualMatcher(SubjectT expected, boolean negated) {
		this(expected, negated, null);
	}

	public DeepEqualMatcher(SubjectT expected, boolean negated, DeepCompareConfig config) {
		this.expected = expected;
		this.negated = negated;
		this.config = config;
	}

	public SubjectT getExpected() {
		return expected;
	}

	public boolean isNegated() {
		return negated;
	}

	public DeepCompareConfig getConfig() {
		return config;
	}

	public void setConfig(DeepCompareConfig config) {
		this.config = config;
	}

	@Override
	public DeepEqualConfigurer<SubjectT> using(DeepCompareConfig config) {
		this.config = config;
		return this;
	}

	@Override
	public SubjectT match(SubjectT actual, boolean assume) {
		if(DeepEquals.deepEquals(actual, expected, config) != negated)
			return actual;
		EqualInfo info = new EqualInfo(expected, actual, negated, config);
		if(assume)
			throw new EqualAssumptionFailureError(info);
		else
			throw new EqualAssertionFailureError(info);
	}

}
