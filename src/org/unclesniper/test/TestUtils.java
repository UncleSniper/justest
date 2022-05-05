package org.unclesniper.test;

import java.io.PrintStream;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import org.unclesniper.test.resource.Localization;

public class TestUtils {

	private static final class WrappedIOException extends RuntimeException {

		WrappedIOException(IOException cause) {
			super(null, cause);
		}

	}

	private static final Pattern ENDLN_PAT = Pattern.compile("\n|\r\n?");

	private static final String L10N_PREFIX = TestUtils.class.getName() + '.';

	private static final String CAUSED_BY_MSG = Localization.DEFAULT.getMessage(L10N_PREFIX + "causedBy");

	private static final String AT_MSG = Localization.DEFAULT.getMessage(L10N_PREFIX + "at");

	private TestUtils() {}

	@SuppressWarnings("unchecked")
	public static <ExceptionT extends Throwable> CapturedOutput captureOutput(Action<? extends ExceptionT> action,
			ExceptionHandler<? extends ExceptionT> exceptionHandler) throws ExceptionT {
		if(action == null)
			throw new IllegalArgumentException("Action must not be null");
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
		if(str == null)
			throw new IllegalArgumentException("String must not be null");
		int length = str.length();
		StringBuilder builder = new StringBuilder();
		for(; length < width; ++length)
			builder.append(padding);
		builder.append(str);
		return builder.toString();
	}

	public static void printStackTrace(Throwable exception, TextWriter out) throws IOException {
		if(exception == null)
			throw new IllegalArgumentException("Exception must not be null");
		if(out == null)
			throw new IllegalArgumentException("Output writer must not be null");
		Throwable t = exception;
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
			StackTraceElement[] trace = t.getStackTrace();
			if(trace != null) {
				for(StackTraceElement frame : trace) {
					if(frame == null)
						continue;
					out.puts("    ");
					out.puts(TestUtils.AT_MSG);
					out.puts(frame.toString());
					out.endln();
				}
			}
			t = t.getCause();
		} while(t != null);
	}

	public static void printStream(Stream<String> lines, TextWriter out) throws IOException {
		if(out == null)
			throw new IllegalArgumentException("Output writer must not be null");
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

}
