package org.unclesniper.test.matcher;

import java.util.function.Consumer;

import static org.unclesniper.test.TestUtils.notNull;

public class ExceptionMatcherSubSink implements Consumer<String> {

	private final Consumer<String> sink;

	private boolean hadLine;

	public ExceptionMatcherSubSink(Consumer<String> sink) {
		this.sink = notNull(sink, "Sink");
	}

	public void reset() {
		hadLine = false;
	}

	@Override
	public void accept(String line) {
		if(line == null)
			return;
		if(hadLine)
			sink.accept("  " + line);
		else {
			sink.accept("- " + line);
			hadLine = true;
		}
	}

}
