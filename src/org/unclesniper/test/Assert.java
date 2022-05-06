package org.unclesniper.test;

import org.unclesniper.test.matcher.Matcher;
import org.unclesniper.test.matcher.Subject;
import org.unclesniper.test.matcher.EqualMatcher;

import static org.unclesniper.test.TestUtils.notNull;

public class Assert {

	private Assert() {}

	public static void fail(String message) {
		throw new UnconditionalAssertionFailureError(message);
	}

	public static <SubT extends SuperT, SuperT> void assertSubtype() {}

	public static <InT, OutT> Matcher<InT, OutT> is(Matcher<InT, OutT> matcher) {
		return notNull(matcher, "Matcher");
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> equalTo(SubjectT expected) {
		return new EqualMatcher<SubjectT>(expected, false);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> notEqualTo(SubjectT unexpected) {
		return new EqualMatcher<SubjectT>(unexpected, true);
	}

	public static <InT, OutT> Subject<OutT>
	assertThat(InT actual, Matcher<? super InT, ? extends OutT> matcher) {
		return new Subject<OutT>(notNull(matcher, "Matcher").match(actual, false), false);
	}

}
