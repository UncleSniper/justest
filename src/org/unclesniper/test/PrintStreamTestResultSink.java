package org.unclesniper.test;

import java.io.PrintStream;

import static org.unclesniper.test.TestUtils.notNull;

public class PrintStreamTestResultSink extends AbstractTextTestResultSink {

	private PrintStream stream;

	public PrintStreamTestResultSink(PrintStream stream) {
		this.stream = stream;
	}

	public PrintStream getStream() {
		return stream;
	}

	public void setStream(PrintStream stream) {
		this.stream = stream;
	}

	private void requireStream() {
		if(stream == null)
			throw new IllegalStateException("No output stream set");
	}

	@Override
	protected void puts(String str) {
		notNull(str, "String to write");
		requireStream();
		stream.print(str);
	}

	@Override
	protected void putch(char[] chars, int offset, int length) {
		notNull(chars, "Character array");
		requireStream();
		stream.print(String.valueOf(chars, offset, length));
	}

	@Override
	protected void endln() {
		requireStream();
		stream.println();
	}

	@Override
	protected void flush() {
		requireStream();
		stream.flush();
	}

}
