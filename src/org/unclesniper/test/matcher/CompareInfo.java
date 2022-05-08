package org.unclesniper.test.matcher;

import org.unclesniper.test.TestUtils;

import static org.unclesniper.test.TestUtils.notNull;

public class CompareInfo extends AbstractInfo {

	private final Object bound;

	private final Object actual;

	private final OrderConstraint relation;

	public CompareInfo(Object bound, Object actual, OrderConstraint relation) {
		this.bound = bound;
		this.actual = actual;
		this.relation = notNull(relation, "Relation");
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

	@Override
	protected void make(Sink sink) {
		sink.append("Expected", false);
		sink.append(TestUtils.describeObject(actual), true);
		sink.append("to be " + relation.getHumanSymbol(), false);
		sink.append(TestUtils.describeObject(bound), true);
		sink.append("but was not", false);
	}

}
