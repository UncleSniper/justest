package org.unclesniper.test;

import java.util.stream.Stream;

public class EmptyCapturedOutput implements CapturedOutput {

	public static final CapturedOutput instance = new EmptyCapturedOutput();

	public EmptyCapturedOutput() {}

	@Override
	public Stream<String> getStandardOutput() {
		return Stream.empty();
	}

	@Override
	public Stream<String> getStandardError() {
		return Stream.empty();
	}

}
