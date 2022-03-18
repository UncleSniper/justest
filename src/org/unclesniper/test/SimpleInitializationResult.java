package org.unclesniper.test;

import java.util.stream.Stream;

public class SimpleInitializationResult implements InitializationResult {

	private final Object base;

	private final Throwable error;

	private final CapturedOutput capturedOutput;

	public SimpleInitializationResult(Object base, Throwable error, CapturedOutput capturedOutput) {
		this.base = base;
		this.error = error;
		this.capturedOutput = capturedOutput;
	}

	@Override
	public Object getBase() {
		return base;
	}

	@Override
	public Throwable getError() {
		return error;
	}

	@Override
	public Stream<String> getStandardOutput() {
		Stream<String> stream = capturedOutput == null ? null : capturedOutput.getStandardOutput();
		return stream == null ? Stream.empty() : stream;
	}

	@Override
	public Stream<String> getStandardError() {
		Stream<String> stream = capturedOutput == null ? null : capturedOutput.getStandardError();
		return stream == null ? Stream.empty() : stream;
	}

}
