package org.unclesniper.test.deepeq;

import static org.unclesniper.test.TestUtils.notNull;

public final class DeepComparePair {

	private final Object a;

	private final Object b;

	public DeepComparePair(Object a, Object b) {
		this.a = notNull(a, "Object A");
		this.b = notNull(b, "Object B");
	}

	@Override
	public int hashCode() {
		return (int)((long)System.identityHashCode(a) + (long)System.identityHashCode(b));
	}

	@Override
	public boolean equals(Object other) {
		if(other == this)
			return true;
		if(other == null || !(other instanceof DeepComparePair))
			return false;
		DeepComparePair dcp = (DeepComparePair)other;
		return (a == dcp.a && b == dcp.b) || (a == dcp.b && b == dcp.a);
	}

}
