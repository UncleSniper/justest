package org.unclesniper.test.deepeq;

public interface DeepCompareConfig {

	DeepComparer getComparer(Class<?> type);

	Iterable<DeepCompareFamily> getCompareFamilies();

	boolean isSkipBuiltin();

}
