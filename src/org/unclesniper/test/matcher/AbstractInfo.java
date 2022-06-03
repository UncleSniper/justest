package org.unclesniper.test.matcher;

import java.util.stream.Stream;
import java.util.function.Supplier;
import org.unclesniper.test.IndentSink;

public abstract class AbstractInfo implements Info {

	public AbstractInfo() {}

	protected abstract void make(IndentSink sink);

	@Override
	public String makeMessage() {
		StringBuilder builder = new StringBuilder();
		make((line, indent) -> {
			if(builder.length() > 0)
				builder.append(' ');
			builder.append(line);
		});
		return builder.toString();
	}

	@Override
	public Supplier<Stream<String>> makeDescription() {
		Stream.Builder<String> builder = Stream.builder();
		make((line, indent) -> builder.accept(indent ? "    " + line : line));
		Stream<String> stream = builder.build();
		return () -> stream;
	}

}
