package org.unclesniper.test;

import java.util.Collection;
import java.util.stream.Stream;
import java.util.function.Supplier;

public class SimpleCapturedOutput implements CapturedOutput {

	private final Supplier<? extends Stream<String>> output;

	private final Supplier<? extends Stream<String>> error;

	public SimpleCapturedOutput(Supplier<? extends Stream<String>> output, Supplier<? extends Stream<String>> error) {
		this.output = output;
		this.error = error;
	}

	public SimpleCapturedOutput(Collection<String> output, Supplier<? extends Stream<String>> error) {
		this.output = () -> output == null ? null : output.stream();
		this.error = error;
	}

	public SimpleCapturedOutput(Supplier<? extends Stream<String>> output, Collection<String> error) {
		this.output = output;
		this.error = () -> error == null ? null : error.stream();
	}

	public SimpleCapturedOutput(Collection<String> output, Collection<String> error) {
		this.output = () -> output == null ? null : output.stream();
		this.error = () -> error == null ? null : error.stream();
	}

	@Override
	public Stream<String> getStandardOutput() {
		Stream<String> stream = output == null ? null : output.get();
		return stream == null ? Stream.empty() : stream;
	}

	@Override
	public Stream<String> getStandardError() {
		Stream<String> stream = error == null ? null : error.get();
		return stream == null ? Stream.empty() : stream;
	}

}
