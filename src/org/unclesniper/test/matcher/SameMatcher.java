package org.unclesniper.test.matcher;

import org.unclesniper.test.TestUtils;
import org.unclesniper.test.IndentSink;

import static org.unclesniper.test.TestUtils.notNull;

public class SameMatcher<SubjectT> implements Matcher<SubjectT, SubjectT> {

	private final SubjectT expected;

	private final boolean negated;

	public SameMatcher(SubjectT expected) {
		this(expected, false);
	}

	public SameMatcher(SubjectT expected, boolean negated) {
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
		if((actual == expected) != negated)
			return actual;
		SameInfo info = new SameInfo(expected, actual, negated);
		if(assume)
			throw new SameAssumptionFailureError(info);
		else
			throw new SameAssertionFailureError(info);
	}

	@Override
	public boolean matches(SubjectT actual) {
		return (actual == expected) != negated;
	}

	@Override
	public void describe(IndentSink sink) {
		notNull(sink, "Sink");
		sink.append(negated ? "not to be the same object as" : "to be the same object as", false);
		sink.append(TestUtils.describeObject(expected), true);
	}

}
