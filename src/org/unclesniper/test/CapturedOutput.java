package org.unclesniper.test;

import java.util.stream.Stream;

public interface CapturedOutput {

	Stream<String> getStandardOutput();

	Stream<String> getStandardError();

}
