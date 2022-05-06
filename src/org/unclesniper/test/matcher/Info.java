package org.unclesniper.test.matcher;

import java.util.stream.Stream;
import java.util.function.Supplier;

public interface Info {

	String makeMessage();

	Supplier<Stream<String>> makeDescription();

}
