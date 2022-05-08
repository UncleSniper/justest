package org.unclesniper.test;

import java.util.Comparator;
import org.unclesniper.test.matcher.Matcher;
import org.unclesniper.test.matcher.Subject;
import org.unclesniper.test.matcher.EqualMatcher;
import org.unclesniper.test.matcher.CompareMatcher;
import org.unclesniper.test.matcher.OrderConstraint;

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

	public static <InT> Subject<InT> assertThat(InT actual) {
		return new Subject<InT>(actual, false);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> orderEqualTo(SubjectT expected,
			Comparator<? super SubjectT> comparator) {
		return new CompareMatcher<SubjectT, SubjectT>(notNull(comparator, "Comparator")::compare, expected,
				OrderConstraint.EQUAL);
	}

	public static <BoundT, SubjectT extends Comparable<? super BoundT>>
	Matcher<SubjectT, SubjectT> orderEqualTo(BoundT expected) {
		return new CompareMatcher<SubjectT, BoundT>(Comparable::compareTo, expected, OrderConstraint.EQUAL);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> orderUnequalTo(SubjectT expected,
			Comparator<? super SubjectT> comparator) {
		return new CompareMatcher<SubjectT, SubjectT>(notNull(comparator, "Comparator")::compare, expected,
				OrderConstraint.UNEQUAL);
	}

	public static <BoundT, SubjectT extends Comparable<? super BoundT>>
	Matcher<SubjectT, SubjectT> orderUnequalTo(BoundT expected) {
		return new CompareMatcher<SubjectT, BoundT>(Comparable::compareTo, expected, OrderConstraint.UNEQUAL);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> lessThan(SubjectT bound,
			Comparator<? super SubjectT> comparator) {
		return new CompareMatcher<SubjectT, SubjectT>(notNull(comparator, "Comparator")::compare, bound,
				OrderConstraint.LESS);
	}

	public static <BoundT, SubjectT extends Comparable<? super BoundT>>
	Matcher<SubjectT, SubjectT> lessThan(BoundT bound) {
		return new CompareMatcher<SubjectT, BoundT>(Comparable::compareTo, bound, OrderConstraint.LESS);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> lessOrEqual(SubjectT bound,
			Comparator<? super SubjectT> comparator) {
		return new CompareMatcher<SubjectT, SubjectT>(notNull(comparator, "Comparator")::compare, bound,
				OrderConstraint.LESS_EQUAL);
	}

	public static <BoundT, SubjectT extends Comparable<? super BoundT>>
	Matcher<SubjectT, SubjectT> lessOrEqual(BoundT bound) {
		return new CompareMatcher<SubjectT, BoundT>(Comparable::compareTo, bound, OrderConstraint.LESS_EQUAL);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> greaterThan(SubjectT bound,
			Comparator<? super SubjectT> comparator) {
		return new CompareMatcher<SubjectT, SubjectT>(notNull(comparator, "Comparator")::compare, bound,
				OrderConstraint.GREATER);
	}

	public static <BoundT, SubjectT extends Comparable<? super BoundT>>
	Matcher<SubjectT, SubjectT> greaterThan(BoundT bound) {
		return new CompareMatcher<SubjectT, BoundT>(Comparable::compareTo, bound, OrderConstraint.GREATER);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> greaterOrEqual(SubjectT bound,
			Comparator<? super SubjectT> comparator) {
		return new CompareMatcher<SubjectT, SubjectT>(notNull(comparator, "Comparator")::compare, bound,
				OrderConstraint.GREATER_EQUAL);
	}

	public static <BoundT, SubjectT extends Comparable<? super BoundT>>
	Matcher<SubjectT, SubjectT> greaterOrEqual(BoundT bound) {
		return new CompareMatcher<SubjectT, BoundT>(Comparable::compareTo, bound, OrderConstraint.GREATER_EQUAL);
	}

}
