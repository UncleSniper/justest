package org.unclesniper.test;

import java.io.PrintStream;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class TestUtils {

	private static final Pattern ENDLN_PAT = Pattern.compile("\n|\r\n?");

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
		String[] outLines = TestUtils.ENDLN_PAT.split(wholeOut);
		String[] errLines = TestUtils.ENDLN_PAT.split(wholeErr);
		return new SimpleCapturedOutput(() -> Stream.of(outLines), () -> Stream.of(errLines));
	}

}
