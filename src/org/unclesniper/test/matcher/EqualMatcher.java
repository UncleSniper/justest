package org.unclesniper.test.matcher;

import java.util.Objects;
import org.unclesniper.test.TestUtils;
import org.unclesniper.test.IndentSink;

import static org.unclesniper.test.TestUtils.notNull;

public class EqualMatcher<SubjectT> implements Matcher<SubjectT, SubjectT> {

	private final SubjectT expected;

	private final boolean negated;

	public EqualMatcher(SubjectT expected) {
		this(expected, false);
	}

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

	@Override
	public boolean matches(SubjectT actual) {
		return Objects.equals(actual, expected) != negated;
	}

	@Override
	public void describe(IndentSink sink) {
		notNull(sink, "Sink");
		sink.append(negated ? "to be unequal to" : "to be equal to", false);
		sink.append(TestUtils.describeObject(expected), true);
	}

}
