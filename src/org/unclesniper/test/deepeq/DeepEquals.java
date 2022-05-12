package org.unclesniper.test.deepeq;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Queue;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.SortedMap;
import java.util.LinkedList;
import java.util.Collection;

import static org.unclesniper.test.TestUtils.notNull;

public class DeepEquals {

	private DeepEquals() {}

	private static final Map<Class<?>, DeepComparer> DEEP_COMPARERS;

	private static final List<DeepCompareFamily> DEEP_COMPARE_FAMILIES;

	public static final DeepComparer BOOLEAN_ARRAY_DEEP_COMPARER = (a, b, seen, config)
			-> Arrays.equals((boolean[])a, (boolean[])b);

	public static final DeepComparer BYTE_ARRAY_DEEP_COMPARER = (a, b, seen, config)
			-> Arrays.equals((byte[])a, (byte[])b);

	public static final DeepComparer SHORT_ARRAY_DEEP_COMPARER = (a, b, seen, config)
			-> Arrays.equals((short[])a, (short[])b);

	public static final DeepComparer INT_ARRAY_DEEP_COMPARER = (a, b, seen, config)
			-> Arrays.equals((int[])a, (int[])b);

	public static final DeepComparer LONG_ARRAY_DEEP_COMPARER = (a, b, seen, config)
			-> Arrays.equals((long[])a, (long[])b);

	public static final DeepComparer CHAR_ARRAY_DEEP_COMPARER = (a, b, seen, config)
			-> Arrays.equals((char[])a, (char[])b);

	public static final DeepComparer FLOAT_ARRAY_DEEP_COMPARER = (a, b, seen, config)
			-> Arrays.equals((float[])a, (float[])b);

	public static final DeepComparer DOUBLE_ARRAY_DEEP_COMPARER = (a, b, seen, config)
			-> Arrays.equals((double[])a, (double[])b);

	public static final DeepComparer OBJECT_ARRAY_DEEP_COMPARER = (a, b, seen, config)
			-> DeepEquals.deepEqualsObjectArray((Object[])a, (Object[])b, seen, config);

	public static final DeepComparer ORDERED_COLLECTION_DEEP_COMPARER = (a, b, seen, config)
			-> DeepEquals.deepEqualsOrderedCollection((Collection)a, (Collection)b, seen, config);

	public static final DeepComparer MAP_ENTRY_DEEP_COMPARER = (a, b, seen, config)
			-> DeepEquals.deepEqualsMapEntry((Map.Entry)a, (Map.Entry)b, seen, config);

	static {
		DEEP_COMPARERS = new HashMap<Class<?>, DeepComparer>();
		DEEP_COMPARERS.put(boolean[].class, BOOLEAN_ARRAY_DEEP_COMPARER);
		DEEP_COMPARERS.put(byte[].class, BYTE_ARRAY_DEEP_COMPARER);
		DEEP_COMPARERS.put(short[].class, SHORT_ARRAY_DEEP_COMPARER);
		DEEP_COMPARERS.put(int[].class, INT_ARRAY_DEEP_COMPARER);
		DEEP_COMPARERS.put(long[].class, LONG_ARRAY_DEEP_COMPARER);
		DEEP_COMPARERS.put(char[].class, CHAR_ARRAY_DEEP_COMPARER);
		DEEP_COMPARERS.put(float[].class, FLOAT_ARRAY_DEEP_COMPARER);
		DEEP_COMPARERS.put(double[].class, DOUBLE_ARRAY_DEEP_COMPARER);
		DEEP_COMPARE_FAMILIES = new LinkedList<DeepCompareFamily>();
		DEEP_COMPARE_FAMILIES.add(DeepEquals::tryDeepEqualsObjectArray);
		DEEP_COMPARE_FAMILIES.add(new CollectionDeepCompareFamily());
		DEEP_COMPARE_FAMILIES.add(new MapDeepCompareFamily());
		DEEP_COMPARE_FAMILIES.add(DeepEquals::tryDeepEqualsMapEntry);
	}

	public static boolean deepEquals(Object a, Object b, DeepCompareConfig config) {
		Set<DeepComparePair> seen = new HashSet<DeepComparePair>();
		return DeepEquals.deepEquals(a, b, seen, config);
	}

	public static boolean deepEquals(Object a, Object b, Set<DeepComparePair> seen, DeepCompareConfig config) {
		notNull(seen, "Seen set");
		if(a == null)
			return b == null;
		if(b == null)
			return false;
		DeepComparePair pair = new DeepComparePair(a, b);
		if(seen.contains(pair))
			return true;
		seen.add(pair);
		try {
			Class<?> aType = a.getClass();
			Class<?> bType = b.getClass();
			// try config
			DeepComparer comparer;
			if(config != null) {
				comparer = config.getComparer(aType);
				if(comparer != null) {
					if(!aType.equals(bType))
						return false;
					return comparer.deepEquals(a, b, seen, config);
				}
				Iterable<DeepCompareFamily> families = config.getCompareFamilies();
				if(families != null) {
					for(DeepCompareFamily family : families) {
						if(family == null)
							continue;
						comparer = family.tryDeepEquals(a, b);
						if(comparer != null)
							return comparer.deepEquals(a, b, seen, config);
					}
				}
			}
			// try builtin
			if(config == null || !config.isSkipBuiltin()) {
				comparer = DeepEquals.DEEP_COMPARERS.get(aType);
				if(comparer != null) {
					if(!aType.equals(bType))
						return false;
					return comparer.deepEquals(a, b, seen, config);
				}
				for(DeepCompareFamily family : DeepEquals.DEEP_COMPARE_FAMILIES) {
					comparer = family.tryDeepEquals(a, b);
					if(comparer != null)
						return comparer.deepEquals(a, b, seen, config);
				}
			}
			return a.equals(b);
		}
		finally {
			seen.remove(pair);
		}
	}

	public static boolean deepEqualsObjectArray(Object[] a, Object[] b, Set<DeepComparePair> seen,
			DeepCompareConfig config) {
		if(notNull(a, "Array A").length != notNull(b, "Array B").length)
			return false;
		for(int i = 0; i < a.length; ++i) {
			if(!DeepEquals.deepEquals(a[i], b[i], seen, config))
				return false;
		}
		return true;
	}

	public static DeepComparer tryDeepEqualsObjectArray(Object a, Object b) {
		Class<?> aType = notNull(a, "Object A").getClass();
		Class<?> bType = notNull(b, "Object B").getClass();
		if(!aType.isArray() || !bType.isArray())
			return null;
		Class<?> aComponent = aType.getComponentType(), bComponent = bType.getComponentType();
		if(aComponent.isPrimitive() || !aComponent.equals(bComponent))
			return null;
		return DeepEquals.OBJECT_ARRAY_DEEP_COMPARER;
	}

	public static boolean deepEqualsOrderedCollection(Collection<?> a, Collection<?> b, Set<DeepComparePair> seen,
			DeepCompareConfig config) {
		if(notNull(a, "Collection A").size() != notNull(b, "Collection B").size())
			return false;
		Iterator<?> aIter = a.iterator();
		if(aIter == null)
			throw new IllegalStateException("Calling " + a.getClass().getName() + ".iterator() returned null");
		Iterator<?> bIter = b.iterator();
		if(aIter == null)
			throw new IllegalStateException("Calling " + b.getClass().getName() + ".iterator() returned null");
		for(;;) {
			if(!aIter.hasNext())
				return !bIter.hasNext();
			if(!bIter.hasNext())
				return false;
			Object aElem = aIter.next(), bElem = bIter.next();
			if(!DeepEquals.deepEquals(aElem, bElem, seen, config))
				return false;
		}
	}

	public static boolean deepEqualsUnorderedCollection(Collection<?> a, Collection<?> b,
			Set<DeepComparePair> seen, DeepCompareConfig config, boolean symmetric) {
		int size = notNull(a, "Collection A").size();
		if(notNull(b, "Collection B").size() != size)
			return false;
		// build up equality matrix
		int ssquare = size * size;
		if(ssquare / size != size || ssquare + 7 < 7)
			throw new IllegalStateException("Cannot address quadratic space for collection size " + size);
		long[] equality = new long[(ssquare + 7) / 8]; // a major
		int aCount = 0;
		for(Object aElem : a) {
			int bCount = 0;
			for(Object bElem : b) {
				if(!symmetric || bCount <= aCount) {
					if(DeepEquals.deepEquals(aElem, bElem, seen, config)) {
						int bit = aCount * size + bCount;
						equality[bit / 8] |= 1L << (bit % 8);
						if(symmetric && aCount != bCount) {
							bit = bCount * size + aCount;
							equality[bit / 8] |= 1L << (bit % 8);
						}
					}
				}
				if(++bCount > size)
					throw new IllegalStateException("Collection of type " + b.getClass().getName()
							+ " claimed to be of size " + size + ", but its iterator generated more elements");
			}
			if(bCount < size)
				throw new IllegalStateException("Collection of type " + b.getClass().getName()
						+ " claimed to be of size " + size + ", but its iterator generated only "
						+ bCount + " elements");
			if(++aCount > size)
				throw new IllegalStateException("Collection of type " + a.getClass().getName()
						+ " claimed to be of size " + size + ", but its iterator generated more elements");
		}
		if(aCount < size)
			throw new IllegalStateException("Collection of type " + a.getClass().getName()
					+ " claimed to be of size " + size + ", but its iterator generated only "
					+ aCount + " elements");
		// figure out bijection
		long[] taken = new long[(size + 7) / 8];
		int[] nextColumn = new int[size];
		int moveRow = 0;
		for(;;) {
			int bit, column = nextColumn[moveRow];
			for(; column < size; ++column) {
				if((taken[column / 8] & (1L << (column % 8))) != 0L)
					continue;
				bit = moveRow * size + column;
				if((equality[bit / 8] & (1L << (bit % 8))) != 0L)
					break;
			}
			if(column >= size) {
				nextColumn[moveRow] = 0;
				if(--moveRow < 0)
					return false;
				bit = nextColumn[moveRow] - 1;
				if(bit > 0)
					taken[bit / 8] &= ~(1L << (bit % 8));
				continue;
			}
			long mask = 1L << (column % 8);
			if((taken[column / 8] & mask) != 0L)
				throw new Error("King Bula lost in time");
			taken[column / 8] |= mask;
			nextColumn[moveRow] = column + 1;
			if(++moveRow >= size)
				return true;
		}
	}

	public static boolean isOrderedCollection(Collection<?> obj) {
		return obj instanceof Queue
				|| obj instanceof List
				|| obj instanceof SortedSet;
	}

	public static boolean deepEqualsMap(Map<?, ?> a, Map<?, ?> b, Set<DeepComparePair> seen,
			DeepCompareConfig config, boolean shortCircuitSorted) {
		Set<?> aEntries = notNull(a, "Map A").entrySet();
		Set<?> bEntries = notNull(b, "Map B").entrySet();
		if(shortCircuitSorted && a instanceof SortedMap && b instanceof SortedMap)
			return DeepEquals.deepEqualsOrderedCollection(aEntries, bEntries, seen, config);
		return DeepEquals.deepEquals(aEntries, bEntries, seen, config);
	}

	public static boolean deepEqualsMapEntry(Map.Entry<?, ?> a, Map.Entry<?, ?> b, Set<DeepComparePair> seen,
			DeepCompareConfig config) {
		notNull(a, "Map entry A");
		notNull(b, "Map entry B");
		return DeepEquals.deepEquals(a.getKey(), b.getKey(), seen, config)
				&& DeepEquals.deepEquals(a.getValue(), b.getValue(), seen, config);
	}

	public static DeepComparer tryDeepEqualsMapEntry(Object a, Object b) {
		return a instanceof Map.Entry && b instanceof Map.Entry ? DeepEquals.MAP_ENTRY_DEEP_COMPARER : null;
	}

}
