package org.unclesniper.test.matcher;

import org.unclesniper.test.IndentSink;

import static org.unclesniper.test.TestUtils.notNull;

public class ChainMatcher<InT, IntermediateT, OutT> implements Matcher<InT, OutT> {

	private final Matcher<? super InT, ? extends IntermediateT> innerMatcher;

	private final Matcher<? super IntermediateT, ? extends OutT> outerMatcher;

	public ChainMatcher(Matcher<? super InT, ? extends IntermediateT> innerMatcher,
			Matcher<? super IntermediateT, ? extends OutT> outerMatcher) {
		this.innerMatcher = notNull(innerMatcher, "Inner matcher");
		this.outerMatcher = notNull(outerMatcher, "Outer matcher");
	}

	public Matcher<? super InT, ? extends IntermediateT> getInnerMatcher() {
		return innerMatcher;
	}

	public Matcher<? super IntermediateT, ? extends OutT> getOuterMatcher() {
		return outerMatcher;
	}

	@Override
	public OutT match(InT subject, boolean assume) {
		return outerMatcher.match(innerMatcher.match(subject, assume), assume);
	}

	@Override
	public boolean matches(InT subject) {
		if(!innerMatcher.matches(subject))
			return false;
		return outerMatcher.matches(innerMatcher.match(subject, false));
	}

	@Override
	public void describe(IndentSink sink) {
		innerMatcher.describe(sink);
	}

}
