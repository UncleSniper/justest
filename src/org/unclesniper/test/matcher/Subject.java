package org.unclesniper.test.matcher;

import java.util.Comparator;
import org.unclesniper.test.Matchers;
import org.unclesniper.test.deepeq.DeepCompareConfig;

import static org.unclesniper.test.TestUtils.notNull;

public class Subject<T> {

	private final T subject;

	private final boolean assume;

	public Subject(T subject, boolean assume) {
		this.subject = subject;
		this.assume = assume;
	}

	public T getSubject() {
		return subject;
	}

	public <OutT> Subject<OutT> is(Matcher<? super T, ? extends OutT> matcher) {
		return new Subject<OutT>(notNull(matcher, "Matcher").match(subject, assume), assume);
	}

	public <OutT> Subject<OutT> that(Matcher<? super T, ? extends OutT> matcher) {
		return is(matcher);
	}

	public <OutT> Subject<OutT> andIs(Matcher<? super T, ? extends OutT> matcher) {
		return is(matcher);
	}

	public <OutT> Subject<OutT> andThat(Matcher<? super T, ? extends OutT> matcher) {
		return is(matcher);
	}

	public Subject<T> isEqualTo(T expected) {
		return is(Matchers.equalTo(expected));
	}

	public Subject<T> isNotEqualTo(T unexpected) {
		return is(Matchers.notEqualTo(unexpected));
	}

	public Subject<T> isDeepEqualTo(T expected) {
		return is(Matchers.deepEqualTo(expected));
	}

	public Subject<T> isDeepEqualTo(T expected, DeepCompareConfig config) {
		return is(Matchers.deepEqualTo(expected, config));
	}

	public Subject<T> isNotDeepEqualTo(T expected) {
		return is(Matchers.notDeepEqualTo(expected));
	}

	public Subject<T> isNotDeepEqualTo(T expected, DeepCompareConfig config) {
		return is(Matchers.notDeepEqualTo(expected, config));
	}

	public Subject<T> isOrderEqualTo(T expected, Comparator<? super T> comparator) {
		return is(Matchers.orderEqualTo(expected, comparator));
	}

	public <B extends Comparable<? super T>> Subject<T> isOrderEqualTo(B expected) {
		return is(new CompareMatcher<T, B>((a, b) -> -b.compareTo(a), expected, OrderConstraint.EQUAL));
	}

	public Subject<T> isOrderUnequalTo(T expected, Comparator<? super T> comparator) {
		return is(Matchers.orderUnequalTo(expected, comparator));
	}

	public <B extends Comparable<? super T>> Subject<T> isOrderUnequalTo(B expected) {
		return is(new CompareMatcher<T, B>((a, b) -> -b.compareTo(a), expected, OrderConstraint.UNEQUAL));
	}

	public Subject<T> isLessThan(T bound, Comparator<? super T> comparator) {
		return is(Matchers.lessThan(bound, comparator));
	}

	public <B extends Comparable<? super T>> Subject<T> isLessThan(B bound) {
		return is(new CompareMatcher<T, B>((a, b) -> -b.compareTo(a), bound, OrderConstraint.LESS));
	}

	public Subject<T> isLessOrEqual(T bound, Comparator<? super T> comparator) {
		return is(Matchers.lessOrEqual(bound, comparator));
	}

	public <B extends Comparable<? super T>> Subject<T> isLessOrEqual(B bound) {
		return is(new CompareMatcher<T, B>((a, b) -> -b.compareTo(a), bound, OrderConstraint.LESS_EQUAL));
	}

	public Subject<T> isGreaterThan(T bound, Comparator<? super T> comparator) {
		return is(Matchers.greaterThan(bound, comparator));
	}

	public <B extends Comparable<? super T>> Subject<T> isGreaterThan(B bound) {
		return is(new CompareMatcher<T, B>((a, b) -> -b.compareTo(a), bound, OrderConstraint.GREATER));
	}

	public Subject<T> isGreaterOrEqual(T bound, Comparator<? super T> comparator) {
		return is(Matchers.greaterOrEqual(bound, comparator));
	}

	public <B extends Comparable<? super T>> Subject<T> isGreaterOrEqual(B bound) {
		return is(new CompareMatcher<T, B>((a, b) -> -b.compareTo(a), bound, OrderConstraint.GREATER_EQUAL));
	}

	public Subject<T> isOfType(Class<?> expectedType) {
		return is(Matchers.ofType(expectedType));
	}

	public Subject<T> isNotOfType(Class<?> expectedType) {
		return is(Matchers.notOfType(expectedType));
	}

	public <OutT> Subject<OutT> isOfSubtype(Class<? extends OutT> expectedType) {
		return is(Matchers.ofSubtype(expectedType));
	}

	public Subject<T> isNull() {
		return is(Matchers.nil());
	}

	public Subject<T> isNotNull() {
		return is(Matchers.notNull());
	}

	public Subject<T> isSameAs(T expected) {
		return is(Matchers.sameAs(expected));
	}

	public Subject<T> isNotSameAs(T expected) {
		return is(Matchers.notSameAs(expected));
	}

}
