package org.unclesniper.test;

import org.unclesniper.test.matcher.Matcher;
import org.unclesniper.test.matcher.Subject;

import static org.unclesniper.test.TestUtils.notNull;

public class Assume {

	private Assume() {}

	public static void skip(String message) {
		throw new UnconditionalAssumptionFailureError(message);
	}

	public static <InT, OutT> Subject<OutT>
	assumeThat(InT actual, Matcher<? super InT, ? extends OutT> matcher) {
		return new Subject<OutT>(notNull(matcher, "Matcher").match(actual, true), true);
	}

}
