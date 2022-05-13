package org.unclesniper.test;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Queue;
import java.util.HashMap;
import java.io.PrintStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.IdentityHashMap;
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

	private interface ObjectDescriber {

		void describe(Object object, StringBuilder sink, Set<Object> seen);

	}

	private static final Pattern ENDLN_PAT = Pattern.compile("\n|\r\n?");

	private static final String L10N_PREFIX = TestUtils.class.getName() + '.';

	private static final String CAUSED_BY_MSG = Localization.DEFAULT.getMessage(L10N_PREFIX + "causedBy");

	private static final String AT_MSG = Localization.DEFAULT.getMessage(L10N_PREFIX + "at");

	private static final ObjectDescriber BOOLEAN_ARRAY_OBJECT_DESCRIBER = (object, sink, seen)
			-> TestUtils.describeBooleanArray((boolean[])object, sink);

	private static final ObjectDescriber BYTE_ARRAY_OBJECT_DESCRIBER = (object, sink, seen)
			-> TestUtils.describeByteArray((byte[])object, sink);

	private static final ObjectDescriber SHORT_ARRAY_OBJECT_DESCRIBER = (object, sink, seen)
			-> TestUtils.describeShortArray((short[])object, sink);

	private static final ObjectDescriber INT_ARRAY_OBJECT_DESCRIBER = (object, sink, seen)
			-> TestUtils.describeIntArray((int[])object, sink);

	private static final ObjectDescriber LONG_ARRAY_OBJECT_DESCRIBER = (object, sink, seen)
			-> TestUtils.describeLongArray((long[])object, sink);

	private static final ObjectDescriber CHAR_ARRAY_OBJECT_DESCRIBER = (object, sink, seen)
			-> TestUtils.describeCharArray((char[])object, sink);

	private static final ObjectDescriber FLOAT_ARRAY_OBJECT_DESCRIBER = (object, sink, seen)
			-> TestUtils.describeFloatArray((float[])object, sink);

	private static final ObjectDescriber DOUBLE_ARRAY_OBJECT_DESCRIBER = (object, sink, seen)
			-> TestUtils.describeDoubleArray((double[])object, sink);

	private static Map<Class<?>, ObjectDescriber> ARRAY_OBJECT_DESCRIBERS;

	static {
		ARRAY_OBJECT_DESCRIBERS = new HashMap<Class<?>, ObjectDescriber>();
		ARRAY_OBJECT_DESCRIBERS.put(Boolean.TYPE, BOOLEAN_ARRAY_OBJECT_DESCRIBER);
		ARRAY_OBJECT_DESCRIBERS.put(Byte.TYPE, BYTE_ARRAY_OBJECT_DESCRIBER);
		ARRAY_OBJECT_DESCRIBERS.put(Short.TYPE, SHORT_ARRAY_OBJECT_DESCRIBER);
		ARRAY_OBJECT_DESCRIBERS.put(Integer.TYPE, INT_ARRAY_OBJECT_DESCRIBER);
		ARRAY_OBJECT_DESCRIBERS.put(Long.TYPE, LONG_ARRAY_OBJECT_DESCRIBER);
		ARRAY_OBJECT_DESCRIBERS.put(Character.TYPE, CHAR_ARRAY_OBJECT_DESCRIBER);
		ARRAY_OBJECT_DESCRIBERS.put(Float.TYPE, FLOAT_ARRAY_OBJECT_DESCRIBER);
		ARRAY_OBJECT_DESCRIBERS.put(Double.TYPE, DOUBLE_ARRAY_OBJECT_DESCRIBER);
	}

	private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

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
		StringBuilder builder = new StringBuilder();
		Set<Object> seen = Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());
		TestUtils.describeObject(object, builder, seen);
		return builder.toString();
	}

	private static void describeObject(Object object, StringBuilder sink, Set<Object> seen) {
		if(object == null) {
			sink.append("<null>");
			return;
		}
		if(seen.contains(object)) {
			sink.append("<circular structure>");
			return;
		}
		seen.add(object);
		try {
			if(object instanceof List || object instanceof Queue) {
				TestUtils.describeSequence((Collection)object, sink, seen);
				return;
			}
			if(object instanceof Collection) {
				TestUtils.describeSet((Collection)object, sink, seen);
				return;
			}
			if(object instanceof Map) {
				TestUtils.describeMap((Map)object, sink, seen);
				return;
			}
			Class<?> type = object.getClass();
			if(type.isArray()) {
				Class<?> ctype = type.getComponentType();
				if(ctype.isPrimitive()) {
					ObjectDescriber describer = TestUtils.ARRAY_OBJECT_DESCRIBERS.get(ctype);
					if(describer == null)
						throw new IllegalStateException("Missing primitive array object describer for array "
								+ "component type: " + ctype.getName());
					describer.describe(object, sink, seen);
				}
				else
					TestUtils.describeObjectArray((Object[])object, sink, seen);
				return;
			}
			String str = object.toString();
			if(str != null) {
				sink.append(str);
				return;
			}
			sink.append("<object of type ");
			sink.append(object.getClass().getName());
			sink.append(" whose toString() returned null>");
		}
		finally {
			seen.remove(object);
		}
	}

	private static void describeSequence(Collection<?> collection, StringBuilder sink, Set<Object> seen) {
		TestUtils.describeCollection(collection, sink, seen, '[', ']');
	}

	private static void describeSet(Collection<?> collection, StringBuilder sink, Set<Object> seen) {
		TestUtils.describeCollection(collection, sink, seen, '{', '}');
	}

	private static void describeMap(Map<?, ?> map, StringBuilder sink, Set<Object> seen) {
		sink.append('{');
		boolean had = false;
		for(Map.Entry<?, ?> entry : map.entrySet()) {
			if(had)
				sink.append(", ");
			else
				had = true;
			TestUtils.describeObject(entry.getKey(), sink, seen);
			sink.append(" -> ");
			TestUtils.describeObject(entry.getValue(), sink, seen);
		}
		sink.append('}');
	}

	private static void describeCollection(Collection<?> collection, StringBuilder sink, Set<Object> seen,
			char openSymbol, char closeSymbol) {
		sink.append(openSymbol);
		boolean had = false;
		for(Object element : collection) {
			if(had)
				sink.append(", ");
			else
				had = true;
			TestUtils.describeObject(element, sink, seen);
		}
		sink.append(closeSymbol);
	}

	private static void describeObjectArray(Object[] array, StringBuilder sink, Set<Object> seen) {
		sink.append('[');
		boolean had = false;
		for(Object element : array) {
			if(had)
				sink.append(", ");
			else
				had = true;
			TestUtils.describeObject(element, sink, seen);
		}
		sink.append(']');
	}

	private static void describeBooleanArray(boolean[] array, StringBuilder sink) {
		sink.append('[');
		boolean had = false;
		for(boolean element : array) {
			if(had)
				sink.append(", ");
			else
				had = true;
			sink.append(element ? "true" : "false");
		}
		sink.append(']');
	}

	private static void describeByteArray(byte[] array, StringBuilder sink) {
		sink.append('[');
		boolean had = false;
		for(byte element : array) {
			if(had)
				sink.append(", ");
			else
				had = true;
			sink.append(String.valueOf((element + 256) % 256));
		}
		sink.append(']');
	}

	private static void describeShortArray(short[] array, StringBuilder sink) {
		sink.append('[');
		boolean had = false;
		for(short element : array) {
			if(had)
				sink.append(", ");
			else
				had = true;
			sink.append(String.valueOf(element));
		}
		sink.append(']');
	}

	private static void describeIntArray(int[] array, StringBuilder sink) {
		sink.append('[');
		boolean had = false;
		for(int element : array) {
			if(had)
				sink.append(", ");
			else
				had = true;
			sink.append(String.valueOf(element));
		}
		sink.append(']');
	}

	private static void describeLongArray(long[] array, StringBuilder sink) {
		sink.append('[');
		boolean had = false;
		for(long element : array) {
			if(had)
				sink.append(", ");
			else
				had = true;
			sink.append(String.valueOf(element));
		}
		sink.append(']');
	}

	private static void describeCharArray(char[] array, StringBuilder sink) {
		sink.append('[');
		boolean had = false;
		for(char element : array) {
			if(had)
				sink.append(", ");
			else
				had = true;
			switch(element) {
				case '\t':
					sink.append("'\\t'");
					break;
				case '\b':
					sink.append("'\\b'");
					break;
				case '\n':
					sink.append("'\\n'");
					break;
				case '\r':
					sink.append("'\\r'");
					break;
				case '\f':
					sink.append("'\\f'");
					break;
				case '\'':
					sink.append("'\\''");
					break;
				case '\\':
					sink.append("'\\\\'");
					break;
				default:
					if(element >= ' ' && element <= '~') {
						sink.append('\'');
						sink.append(element);
					}
					else {
						int code = (int)element;
						sink.append("'\\u");
						for(int i = 3; i >= 0; --i)
							sink.append(TestUtils.HEX_CHARS[(code >> (i * 4)) & 0xF]);
					}
					sink.append('\'');
					break;
			}
		}
		sink.append(']');
	}

	private static void describeFloatArray(float[] array, StringBuilder sink) {
		sink.append('[');
		boolean had = false;
		for(float element : array) {
			if(had)
				sink.append(", ");
			else
				had = true;
			sink.append(String.valueOf(element));
		}
		sink.append(']');
	}

	private static void describeDoubleArray(double[] array, StringBuilder sink) {
		sink.append('[');
		boolean had = false;
		for(double element : array) {
			if(had)
				sink.append(", ");
			else
				had = true;
			sink.append(String.valueOf(element));
		}
		sink.append(']');
	}

	public static <T> T notNull(T value, String name) {
		if(value == null)
			throw new IllegalArgumentException(name + " must not be null");
		return value;
	}

}
