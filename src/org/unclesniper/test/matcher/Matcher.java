package org.unclesniper.test.matcher;

import org.unclesniper.test.IndentSink;

public interface Matcher<InT, OutT> {

	OutT match(InT subject, boolean assume);

	boolean matches(InT subject);

	void describe(IndentSink sink);

	default <UltimateT> Matcher<InT, UltimateT> that(Matcher<? super OutT, ? extends UltimateT> outerMatcher) {
		return new ChainMatcher<InT, OutT, UltimateT>(this, outerMatcher);
	}

	default <UltimateT> Matcher<InT, UltimateT> andThat(Matcher<? super OutT, ? extends UltimateT> outerMatcher) {
		return this.that(outerMatcher);
	}

}
