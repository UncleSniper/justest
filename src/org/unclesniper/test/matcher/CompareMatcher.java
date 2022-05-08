package org.unclesniper.test.matcher;

import java.util.function.ToIntBiFunction;

import static org.unclesniper.test.TestUtils.notNull;

public class CompareMatcher<SubjectT, BoundT> implements Matcher<SubjectT, SubjectT> {

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
		int actualRelation = comparator.applyAsInt(actual, bound);
		if(relation.isSatisfiedBy(actualRelation))
			return actual;
		CompareInfo info = new CompareInfo(bound, actual, relation);
		if(assume)
			throw new CompareAssumptionFailureError(info);
		else
			throw new CompareAssertionFailureError(info);
	}

}
