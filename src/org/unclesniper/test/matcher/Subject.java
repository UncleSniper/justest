package org.unclesniper.test.matcher;

import java.util.Comparator;
import org.unclesniper.test.Assert;

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

	public <OutT> Subject<OutT> andIs(Matcher<? super T, ? extends OutT> matcher) {
		return is(matcher);
	}

	public <OutT> Subject<OutT> andThat(Matcher<? super T, ? extends OutT> matcher) {
		return is(matcher);
	}

	public Subject<T> isEqualTo(T expected) {
		return is(Assert.equalTo(expected));
	}

	public Subject<T> isNotEqualTo(T unexpected) {
		return is(Assert.notEqualTo(unexpected));
	}

	public Subject<T> isOrderEqualTo(T expected, Comparator<? super T> comparator) {
		return is(Assert.orderEqualTo(expected, comparator));
	}

	public <B extends Comparable<? super T>> Subject<T> isOrderEqualTo(B expected) {
		return is(new CompareMatcher<T, B>((a, b) -> -b.compareTo(a), expected, OrderConstraint.EQUAL));
	}

	public Subject<T> isOrderUnequalTo(T expected, Comparator<? super T> comparator) {
		return is(Assert.orderUnequalTo(expected, comparator));
	}

	public <B extends Comparable<? super T>> Subject<T> isOrderUnequalTo(B expected) {
		return is(new CompareMatcher<T, B>((a, b) -> -b.compareTo(a), expected, OrderConstraint.UNEQUAL));
	}

	public Subject<T> isLessThan(T bound, Comparator<? super T> comparator) {
		return is(Assert.lessThan(bound, comparator));
	}

	public <B extends Comparable<? super T>> Subject<T> isLessThan(B bound) {
		return is(new CompareMatcher<T, B>((a, b) -> -b.compareTo(a), bound, OrderConstraint.LESS));
	}

	public Subject<T> isLessOrEqual(T bound, Comparator<? super T> comparator) {
		return is(Assert.lessOrEqual(bound, comparator));
	}

	public <B extends Comparable<? super T>> Subject<T> isLessOrEqual(B bound) {
		return is(new CompareMatcher<T, B>((a, b) -> -b.compareTo(a), bound, OrderConstraint.LESS_EQUAL));
	}

	public Subject<T> isGreaterThan(T bound, Comparator<? super T> comparator) {
		return is(Assert.greaterThan(bound, comparator));
	}

	public <B extends Comparable<? super T>> Subject<T> isGreaterThan(B bound) {
		return is(new CompareMatcher<T, B>((a, b) -> -b.compareTo(a), bound, OrderConstraint.GREATER));
	}

	public Subject<T> isGreaterOrEqual(T bound, Comparator<? super T> comparator) {
		return is(Assert.greaterOrEqual(bound, comparator));
	}

	public <B extends Comparable<? super T>> Subject<T> isGreaterOrEqual(B bound) {
		return is(new CompareMatcher<T, B>((a, b) -> -b.compareTo(a), bound, OrderConstraint.GREATER_EQUAL));
	}

}
