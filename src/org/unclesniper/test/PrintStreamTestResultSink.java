package org.unclesniper.test;

import java.io.PrintStream;

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

	@Override
	protected void puts(String str) {
		if(str == null)
			throw new IllegalArgumentException("String to write must not be null");
		if(stream == null)
			throw new IllegalStateException("No output stream set");
		stream.print(str);
	}

	@Override
	protected void putch(char[] chars, int offset, int length) {
		if(chars == null)
			throw new IllegalArgumentException("Character array must not be null");
		if(stream == null)
			throw new IllegalStateException("No output stream set");
		stream.print(String.valueOf(chars, offset, length));
	}

	@Override
	protected void endln() {
		if(stream == null)
			throw new IllegalStateException("No output stream set");
		stream.println();
	}

	@Override
	protected void flush() {
		if(stream == null)
			throw new IllegalStateException("No output stream set");
		stream.flush();
	}

}
