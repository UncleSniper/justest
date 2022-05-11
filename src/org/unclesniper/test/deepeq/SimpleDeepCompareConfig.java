package org.unclesniper.test.deepeq;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Collections;

import static org.unclesniper.test.TestUtils.notNull;

public class SimpleDeepCompareConfig implements DeepCompareConfig {

	private final Map<Class<?>, DeepComparer> comparers = new HashMap<Class<?>, DeepComparer>();

	private final List<DeepCompareFamily> compareFamilies = new LinkedList<DeepCompareFamily>();

	private final List<DeepCompareFamily> constCompareFamilies = Collections.unmodifiableList(compareFamilies);

	private boolean skipBuiltin;

	public SimpleDeepCompareConfig() {}

	public SimpleDeepCompareConfig(boolean skipBuiltin) {
		this.skipBuiltin = skipBuiltin;
	}

	public void putComparer(Class<?> type, DeepComparer comparer) {
		comparers.put(notNull(type, "Type"), notNull(comparer, "Comparer"));
	}

	public void addCompareFamily(DeepCompareFamily family) {
		compareFamilies.add(notNull(family, "Family"));
	}

	public void setSkipBuiltin(boolean skipBuiltin) {
		this.skipBuiltin = skipBuiltin;
	}

	@Override
	public DeepComparer getComparer(Class<?> type) {
		return comparers.get(type);
	}

	@Override
	public Iterable<DeepCompareFamily> getCompareFamilies() {
		return constCompareFamilies;
	}

	@Override
	public boolean isSkipBuiltin() {
		return skipBuiltin;
	}

}
