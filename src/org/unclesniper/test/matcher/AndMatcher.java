package org.unclesniper.test.matcher;

import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import org.unclesniper.test.IndentSink;
import org.unclesniper.test.BulletIndentSink;

import static org.unclesniper.test.TestUtils.notNull;

public class AndMatcher<SubjectT> implements Matcher<SubjectT, SubjectT> {

	private final List<Matcher<? super SubjectT, ?>> matchers = new LinkedList<Matcher<? super SubjectT, ?>>();

	public AndMatcher() {}

	public AndMatcher(Iterable<? extends Matcher<? super SubjectT, ?>> matchers) {
		if(matchers != null) {
			for(Matcher<? super SubjectT, ?> matcher : matchers)
				addMatcher(matcher);
		}
	}

	@SafeVarargs
	public AndMatcher(Matcher<? super SubjectT, ?>... matchers) {
		if(matchers != null) {
			for(Matcher<? super SubjectT, ?> matcher : matchers)
				addMatcher(matcher);
		}
	}

	public List<Matcher<? super SubjectT, ?>> getMatchers() {
		return Collections.unmodifiableList(matchers);
	}

	public void clearMatchers() {
		matchers.clear();
	}

	public void addMatcher(Matcher<? super SubjectT, ?> matcher) {
		if(matcher != null)
			matchers.add(matcher);
	}

	private void requireMatchers() {
		if(matchers.isEmpty())
			throw new IllegalStateException("No matchers added to AndMatcher");
	}

	@Override
	public SubjectT match(SubjectT subject, boolean assume) {
		requireMatchers();
		for(Matcher<? super SubjectT, ?> matcher : matchers)
			matcher.match(subject, assume);
		return subject;
	}

	@Override
	public boolean matches(SubjectT subject) {
		requireMatchers();
		for(Matcher<? super SubjectT, ?> matcher : matchers) {
			if(!matcher.matches(subject))
				return false;
		}
		return true;
	}

	@Override
	public void describe(IndentSink sink) {
		notNull(sink, "Sink");
		requireMatchers();
		sink.append("to satisfy all of:", false);
		BulletIndentSink bullet = new BulletIndentSink(sink, true);
		for(Matcher<? super SubjectT, ?> matcher : matchers) {
			bullet.reset();
			matcher.describe(bullet);
		}
	}

}
