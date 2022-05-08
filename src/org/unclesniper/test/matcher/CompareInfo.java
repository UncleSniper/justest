package org.unclesniper.test.matcher;

import org.unclesniper.test.TestUtils;

import static org.unclesniper.test.TestUtils.notNull;

public class CompareInfo extends AbstractInfo {

	private final Object bound;

	private final Object actual;

	private final OrderConstraint relation;

	private final Throwable comparisonException;

	public CompareInfo(Object bound, Object actual, OrderConstraint relation, Throwable comparisonException) {
		this.bound = bound;
		this.actual = actual;
		this.relation = notNull(relation, "Relation");
		this.comparisonException = comparisonException;
	}

	public Object getBound() {
		return bound;
	}

	public Object getActual() {
		return actual;
	}

	public OrderConstraint getRelation() {
		return relation;
	}

	public Throwable getComparisonException() {
		return comparisonException;
	}

	@Override
	protected void make(Sink sink) {
		sink.append("Expected", false);
		sink.append(TestUtils.describeObject(actual), true);
		sink.append("to be " + relation.getHumanSymbol(), false);
		sink.append(TestUtils.describeObject(bound), true);
		if(comparisonException == null)
			sink.append("but was not", false);
		else {
			String message = comparisonException.getMessage();
			if(message == null || message.length() == 0)
				sink.append("but failed to compare", false);
			else {
				sink.append("but failed to compare:", false);
				sink.append(message, true);
			}
		}
	}

}
