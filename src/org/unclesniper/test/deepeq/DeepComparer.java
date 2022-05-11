package org.unclesniper.test.deepeq;

import java.util.Set;

public interface DeepComparer {

	boolean deepEquals(Object a, Object b, Set<DeepComparePair> seen, DeepCompareConfig config);

}
