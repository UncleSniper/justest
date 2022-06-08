package org.unclesniper.test.matcher;

import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import org.unclesniper.test.IndentSink;
import org.unclesniper.test.Describeable;
import org.unclesniper.test.BulletIndentSink;
import org.unclesniper.test.AssertionFailureError;
import org.unclesniper.test.AssumptionFailureError;

import static org.unclesniper.test.TestUtils.notNull;

public class OrMatcher<SubjectT> implements Matcher<SubjectT, SubjectT> {

	private final List<Matcher<? super SubjectT, ?>> matchers = new LinkedList<Matcher<? super SubjectT, ?>>();

	public OrMatcher() {}

	public OrMatcher(Iterable<? extends Matcher<? super SubjectT, ?>> matchers) {
		if(matchers != null) {
			for(Matcher<? super SubjectT, ?> matcher : matchers)
				addMatcher(matcher);
		}
	}

	@SafeVarargs
	public OrMatcher(Matcher<? super SubjectT, ?>... matchers) {
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
			throw new IllegalStateException("No matchers added to OrMatcher");
	}

	@Override
	public SubjectT match(SubjectT subject, boolean assume) {
		requireMatchers();
		List<Describeable> failures = new LinkedList<Describeable>();
		for(Matcher<? super SubjectT, ?> matcher : matchers) {
			try {
				matcher.match(subject, assume);
				return subject;
			}
			catch(AssertionFailureError e) {
				if(assume)
					throw e;
				failures.add(e);
			}
			catch(AssumptionFailureError e) {
				if(!assume)
					throw e;
				failures.add(e);
			}
		}
		OrInfo info = new OrInfo(subject);
		for(Describeable failure : failures)
			info.addFailure(failure);
		if(assume)
			throw new OrAssumptionFailureError(info);
		else
			throw new OrAssertionFailureError(info);
	}

	@Override
	public boolean matches(SubjectT subject) {
		requireMatchers();
		for(Matcher<? super SubjectT, ?> matcher : matchers) {
			if(matcher.matches(subject))
				return true;
		}
		return false;
	}

	@Override
	public void describe(IndentSink sink) {
		notNull(sink, "Sink");
		requireMatchers();
		sink.append("to satisfy any of:", false);
		BulletIndentSink bullet = new BulletIndentSink(sink, true);
		for(Matcher<? super SubjectT, ?> matcher : matchers) {
			bullet.reset();
			matcher.describe(bullet);
		}
	}

}
