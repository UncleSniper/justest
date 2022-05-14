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

	public static <InT> Subject<InT> assumeThat(InT actual) {
		return new Subject<InT>(actual, true);
	}

	public static <InT, OutT> Subject<OutT>
	assumeThat(GenSource<? extends InT> actual, Matcher<? super InT, ? extends OutT> matcher) throws Throwable {
		notNull(actual, "Actual value generator");
		notNull(matcher, "Matcher");
		InT actualValue;
		try {
			actualValue = actual.draw();
		}
		catch(Throwable t) {
			throw new DrawAssumptionFailureError(t);
		}
		return assumeThat(actualValue, matcher);
	}

	public static <InT> Subject<InT> assumeThat(GenSource<? extends InT> actual) {
		notNull(actual, "Actual value generator");
		try {
			return new Subject<InT>(actual.draw(), true);
		}
		catch(Throwable t) {
			throw new DrawAssumptionFailureError(t);
		}
	}

}
