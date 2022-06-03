package org.unclesniper.test.matcher;

import org.unclesniper.test.IndentSink;

public interface Matcher<InT, OutT> {

	OutT match(InT subject, boolean assume);

	boolean matches(InT subject);

	void describe(IndentSink sink);

}
