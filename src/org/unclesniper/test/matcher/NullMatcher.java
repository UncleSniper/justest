package org.unclesniper.test.matcher;

import org.unclesniper.test.IndentSink;

import static org.unclesniper.test.TestUtils.notNull;

public class NullMatcher<SubjectT> implements Matcher<SubjectT, SubjectT> {

	private final boolean negated;

	public NullMatcher(boolean negated) {
		this.negated = negated;
	}

	public boolean isNegated() {
		return negated;
	}

	@Override
	public SubjectT match(SubjectT actual, boolean assume) {
		if((actual == null) != negated)
			return actual;
		NullInfo info = new NullInfo(actual, negated);
		if(assume)
			throw new NullAssumptionFailureError(info);
		else
			throw new NullAssertionFailureError(info);
	}

	@Override
	public boolean matches(SubjectT actual) {
		return (actual == null) != negated;
	}

	@Override
	public void describe(IndentSink sink) {
		notNull(sink, "Sink");
		sink.append(negated ? "not to be null" : "to be null", false);
	}

}
