package org.unclesniper.test;

import java.util.function.Consumer;

import static org.unclesniper.test.TestUtils.notNull;

@FunctionalInterface
public interface GenSink<InT> {

	void take(InT arg) throws Throwable;

	public static <InT> GenSink<InT> of(Consumer<? super InT> sink) {
		notNull(sink, "Sink");
		return arg -> sink.accept(arg);
	}

}
