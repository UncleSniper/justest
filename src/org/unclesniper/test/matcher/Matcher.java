package org.unclesniper.test.matcher;

public interface Matcher<InT, OutT> {

	OutT match(InT subject, boolean assume);

}
