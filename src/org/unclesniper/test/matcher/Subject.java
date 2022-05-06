package org.unclesniper.test.matcher;

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

}
