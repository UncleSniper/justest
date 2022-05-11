package org.unclesniper.test.deepeq;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

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

}
