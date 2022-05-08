package org.unclesniper.test;

import org.unclesniper.test.matcher.Subject;
import org.unclesniper.test.matcher.Matcher;

import static org.unclesniper.test.TestUtils.notNull;

public class Assert {

	private Assert() {}

	public static void fail(String message) {
		throw new UnconditionalAssertionFailureError(message);
	}

	public static <SubT extends SuperT, SuperT> void assertSubtype() {}

	public static <InT, OutT> Subject<OutT>
	assertThat(InT actual, Matcher<? super InT, ? extends OutT> matcher) {
		return new Subject<OutT>(notNull(matcher, "Matcher").match(actual, false), false);
	}

	public static <InT> Subject<InT> assertThat(InT actual) {
		return new Subject<InT>(actual, false);
	}

}
