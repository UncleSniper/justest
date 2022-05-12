package org.unclesniper.test.deepeq;

import java.util.Map;

public class MapDeepCompareFamily implements DeepCompareFamily {

	private boolean shortCircuitSorted;

	public MapDeepCompareFamily() {
		shortCircuitSorted = true;
	}

	public MapDeepCompareFamily(boolean shortCircuitSorted) {
		this.shortCircuitSorted = shortCircuitSorted;
	}

	public boolean isShortCircuitSorted() {
		return shortCircuitSorted;
	}

	public void setShortCircuitSorted(boolean shortCircuitSorted) {
		this.shortCircuitSorted = shortCircuitSorted;
	}

	@Override
	public DeepComparer tryDeepEquals(Object a, Object b) {
		if(!(a instanceof Map) || !(b instanceof Map))
			return null;
		return (a2, b2, seen, config)
				-> DeepEquals.deepEqualsMap((Map)a2, (Map)b2, seen, config, shortCircuitSorted);
	}

}
