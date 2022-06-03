package org.unclesniper.test.matcher;

import org.unclesniper.test.TestUtils;
import org.unclesniper.test.IndentSink;
import org.unclesniper.test.deepeq.DeepCompareConfig;

public class EqualInfo extends AbstractInfo {

	private final Object expected;

	private final Object actual;

	private final boolean negated;

	private final boolean deep;

	private final DeepCompareConfig deepConfig;

	private EqualInfo(Object expected, Object actual, boolean negated, boolean deep, DeepCompareConfig deepConfig) {
		this.expected = expected;
		this.actual = actual;
		this.negated = negated;
		this.deep = deep;
		this.deepConfig = deepConfig;
	}

	public EqualInfo(Object expected, Object actual, boolean negated) {
		this(expected, actual, negated, false, null);
	}

	public EqualInfo(Object expected, Object actual, boolean negated, DeepCompareConfig config) {
		this(expected, actual, negated, true, config);
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

	public boolean isDeep() {
		return deep;
	}

	public DeepCompareConfig getDeepConfig() {
		return deepConfig;
	}

	@Override
	protected void make(IndentSink sink) {
		sink.append("Expected", false);
		sink.append(TestUtils.describeObject(actual), true);
		if(deep)
			sink.append(negated ? "to be deep-unequal to" : "to be deep-equal to", false);
		else
			sink.append(negated ? "to be unequal to" : "to be equal to", false);
		sink.append(TestUtils.describeObject(expected), true);
		sink.append("but was not", false);
	}

}
