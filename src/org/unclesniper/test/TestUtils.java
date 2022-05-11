package org.unclesniper.test;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.io.PrintStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.function.Function;
import java.util.function.Predicate;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import org.unclesniper.test.resource.Localization;

public class TestUtils {

	private static final class WrappedIOException extends RuntimeException {

		WrappedIOException(IOException cause) {
			super(null, cause);
		}

	}

	public interface DeepComparer {

		boolean deepEquals(Object a, Object b, Set<DeepComparePair> seen, DeepCompareConfig config);

	}

	public interface DeepCompareFamily {

		DeepComparer tryDeepEquals(Object a, Object b);

	}

	public static final class DeepComparePair {

		private final Object a;

		private final Object b;

		public DeepComparePair(Object a, Object b) {
			this.a = TestUtils.notNull(a, "Object A");
			this.b = TestUtils.notNull(b, "Object B");
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

	public interface DeepCompareConfig {

		DeepComparer getComparer(Class<?> type);

		Iterable<DeepCompareFamily> getCompareFamilies();

		boolean isSkipBuiltin();

	}

	public static class SimpleDeepCompareConfig implements DeepCompareConfig {

		private final Map<Class<?>, DeepComparer> comparers = new HashMap<Class<?>, DeepComparer>();

		private final List<DeepCompareFamily> compareFamilies = new LinkedList<DeepCompareFamily>();

		private final List<DeepCompareFamily> constCompareFamilies = Collections.unmodifiableList(compareFamilies);

		private boolean skipBuiltin;

		public SimpleDeepCompareConfig() {}

		public SimpleDeepCompareConfig(boolean skipBuiltin) {
			this.skipBuiltin = skipBuiltin;
		}

		public void putComparer(Class<?> type, DeepComparer comparer) {
			comparers.put(TestUtils.notNull(type, "Type"), TestUtils.notNull(comparer, "Comparer"));
		}

		public void addCompareFamily(DeepCompareFamily family) {
			compareFamilies.add(TestUtils.notNull(family, "Family"));
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

	private static final Pattern ENDLN_PAT = Pattern.compile("\n|\r\n?");

	private static final String L10N_PREFIX = TestUtils.class.getName() + '.';

	private static final String CAUSED_BY_MSG = Localization.DEFAULT.getMessage(L10N_PREFIX + "causedBy");

	private static final String AT_MSG = Localization.DEFAULT.getMessage(L10N_PREFIX + "at");

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
			-> TestUtils.deepEqualsObjectArray((Object[])a, (Object[])b, seen, config);

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
		DEEP_COMPARE_FAMILIES.add(TestUtils::tryDeepEqualsObjectArray);
	}

	private TestUtils() {}

	@SuppressWarnings("unchecked")
	public static <ExceptionT extends Throwable> CapturedOutput captureOutput(Action<? extends ExceptionT> action,
			ExceptionHandler<? extends ExceptionT> exceptionHandler) throws ExceptionT {
		notNull(action, "Action");
		ByteArrayOutputStream outBAOS = new ByteArrayOutputStream(), errBAOS = new ByteArrayOutputStream();
		PrintStream outPS, errPS;
		try {
			outPS = new PrintStream(outBAOS, false, "UTF-8");
			errPS = new PrintStream(errBAOS, false, "UTF-8");
		}
		catch(UnsupportedEncodingException uee) {
			throw new Error("JVM does not support UTF-8!?", uee);
		}
		PrintStream oldOut = System.out;
		System.setOut(outPS);
		try {
			PrintStream oldErr = System.err;
			System.setErr(errPS);
			try {
				action.perform();
			}
			catch(Throwable t) {
				if(exceptionHandler != null)
					exceptionHandler.handleException(t);
				else
					throw (ExceptionT)t;
			}
			finally {
				System.setErr(oldErr);
			}
		}
		finally {
			System.setOut(oldOut);
		}
		String wholeOut, wholeErr;
		try {
			wholeOut = outBAOS.toString("UTF-8");
			wholeErr = errBAOS.toString("UTF-8");
		}
		catch(UnsupportedEncodingException uee) {
			throw new Error("JVM does not support UTF-8!?", uee);
		}
		String[] outLines = wholeOut.length() == 0 ? new String[0] : TestUtils.ENDLN_PAT.split(wholeOut);
		String[] errLines = wholeErr.length() == 0 ? new String[0] : TestUtils.ENDLN_PAT.split(wholeErr);
		return new SimpleCapturedOutput(() -> Stream.of(outLines), () -> Stream.of(errLines));
	}

	public static int digitCount(int n, boolean allowNegative) {
		int count;
		if(n < 0) {
			if(!allowNegative)
				throw new IllegalArgumentException("Argument should not be negative: " + n);
			count = 1;
			n = -n;
		}
		else if(n == 0)
			return 1;
		else
			count = 0;
		for(; n > 0; n /= 10)
			++count;
		return count;
	}

	public static String padLeft(String str, char padding, int width) {
		int length = notNull(str, "String").length();
		StringBuilder builder = new StringBuilder();
		for(; length < width; ++length)
			builder.append(padding);
		builder.append(str);
		return builder.toString();
	}

	public static void printStackTrace(Throwable exception, TextWriter out,
			Predicate<StackTraceElement> isFrameInternal, Function<String, String> internalFrameMapper)
					throws IOException {
		Throwable t = notNull(exception, "Exception");
		notNull(out, "Output writer");
		do {
			if(t != exception)
				out.puts(TestUtils.CAUSED_BY_MSG);
			String str = t.toString();
			if(str != null)
				out.puts(str);
			else {
				out.puts(t.getClass().getName());
				String msg = t.getMessage();
				if(msg != null && msg.length() > 0) {
					out.puts(": ");
					out.puts(msg);
				}
			}
			out.endln();
			if(t instanceof Describeable) {
				Stream<String> description = ((Describeable)t).describe();
				if(description != null)
					TestUtils.printStream(description.map(line -> ">> " + line), out);
			}
			StackTraceElement[] trace = t.getStackTrace();
			if(trace != null) {
				boolean hadExternalFrame = false;
				for(StackTraceElement frame : trace) {
					if(frame == null)
						continue;
					boolean internal;
					if(hadExternalFrame)
						internal = false;
					else if(isFrameInternal != null && isFrameInternal.test(frame))
						internal = true;
					else {
						internal = false;
						hadExternalFrame = true;
					}
					String line;
					if(!internal)
						line = TestUtils.AT_MSG + frame;
					else if(internalFrameMapper == null)
						line = null;
					else
						line = internalFrameMapper.apply(TestUtils.AT_MSG + frame);
					if(line == null)
						continue;
					out.puts("    ");
					out.puts(line);
					out.endln();
				}
			}
			t = t.getCause();
		} while(t != null);
	}

	public static void printStream(Stream<String> lines, TextWriter out) throws IOException {
		notNull(out, "Output writer");
		if(lines == null)
			return;
		try {
			lines.forEach(line -> {
				if(line == null)
					return;
				try {
					out.puts(line);
					out.endln();
				}
				catch(IOException ioe) {
					throw new WrappedIOException(ioe);
				}
			});
		}
		catch(WrappedIOException wioe) {
			throw (IOException)wioe.getCause();
		}
	}

	public static String describeObject(Object object) {
		if(object == null)
			return "<null>";
		String str = object.toString();
		if(str != null)
			return str;
		return "<object of type " + object.getClass().getName() + " whose toString() returned null>";
	}

	public static <T> T notNull(T value, String name) {
		if(value == null)
			throw new IllegalArgumentException(name + " must not be null");
		return value;
	}

	public static boolean deepEquals(Object a, Object b, DeepCompareConfig config) {
		Set<DeepComparePair> seen = new HashSet<DeepComparePair>();
		return TestUtils.deepEquals(a, b, seen, config);
	}

	public static boolean deepEquals(Object a, Object b, Set<DeepComparePair> seen, DeepCompareConfig config) {
		TestUtils.notNull(seen, "Seen set");
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
				comparer = TestUtils.DEEP_COMPARERS.get(aType);
				if(comparer != null) {
					if(!aType.equals(bType))
						return false;
					return comparer.deepEquals(a, b, seen, config);
				}
				for(DeepCompareFamily family : TestUtils.DEEP_COMPARE_FAMILIES) {
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
		if(TestUtils.notNull(a, "Array A").length != TestUtils.notNull(b, "Array B").length)
			return false;
		for(int i = 0; i < a.length; ++i) {
			if(!TestUtils.deepEquals(a[i], b[i], seen, config))
				return false;
		}
		return true;
	}

	public static DeepComparer tryDeepEqualsObjectArray(Object a, Object b) {
		Class<?> aType = TestUtils.notNull(a, "Object A").getClass();
		Class<?> bType = TestUtils.notNull(b, "Object B").getClass();
		if(!aType.isArray() || !bType.isArray())
			return null;
		Class<?> aComponent = aType.getComponentType(), bComponent = bType.getComponentType();
		if(aComponent.isPrimitive() || !aComponent.equals(bComponent))
			return null;
		return TestUtils.OBJECT_ARRAY_DEEP_COMPARER;
	}

}
