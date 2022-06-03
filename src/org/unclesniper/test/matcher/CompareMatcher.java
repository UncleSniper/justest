package org.unclesniper.test.matcher;

import org.unclesniper.test.TestUtils;
import org.unclesniper.test.IndentSink;
import java.util.function.ToIntBiFunction;

import static org.unclesniper.test.TestUtils.notNull;

public class CompareMatcher<SubjectT, BoundT> implements Matcher<SubjectT, SubjectT> {

	public static class ComparisonNullPointerException extends IllegalArgumentException {

		public ComparisonNullPointerException(String message) {
			super(message);
		}

		public ComparisonNullPointerException(String message, Throwable cause) {
			super(message, cause);
		}

	}

	private final ToIntBiFunction<? super SubjectT, ? super BoundT> comparator;

	private final BoundT bound;

	private final OrderConstraint relation;

	public CompareMatcher(ToIntBiFunction<? super SubjectT, ? super BoundT> comparator, BoundT bound,
			OrderConstraint relation) {
		this.comparator = notNull(comparator, "Comparator");
		this.bound = bound;
		this.relation = notNull(relation, "Relation");
	}

	public ToIntBiFunction<? super SubjectT, ? super BoundT> getComparator() {
		return comparator;
	}

	public BoundT getBound() {
		return bound;
	}

	public OrderConstraint getRelation() {
		return relation;
	}

	@Override
	public SubjectT match(SubjectT actual, boolean assume) {
		Throwable comparisonException = null;
		int actualRelation = 0;
		try {
			actualRelation = comparator.applyAsInt(actual, bound);
		}
		catch(NullPointerException npe) {
			if(actual == null || bound == null)
				comparisonException = new ComparisonNullPointerException("Calling comparator with null argument "
						+ "caused NullPointerException", npe);
			else
				comparisonException = npe;
		}
		catch(Throwable t) {
			comparisonException = t;
		}
		if(comparisonException == null && relation.isSatisfiedBy(actualRelation))
			return actual;
		CompareInfo info = new CompareInfo(bound, actual, relation, comparisonException);
		if(assume)
			throw new CompareAssumptionFailureError(info, comparisonException);
		else
			throw new CompareAssertionFailureError(info, comparisonException);
	}

	@Override
	public boolean matches(SubjectT actual) {
		try {
			return relation.isSatisfiedBy(comparator.applyAsInt(actual, bound));
		}
		catch(Throwable t) {
			return false;
		}
	}

	@Override
	public void describe(IndentSink sink) {
		notNull(sink, "Sink");
		sink.append("to be " + relation.getHumanSymbol(), false);
		sink.append(TestUtils.describeObject(bound), true);
	}

}
