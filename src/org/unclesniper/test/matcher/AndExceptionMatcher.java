package org.unclesniper.test.matcher;

import java.util.List;
import java.util.LinkedList;
import java.util.function.Consumer;
import org.unclesniper.test.IndentSink;
import org.unclesniper.test.BulletIndentSink;

public class AndExceptionMatcher<SubjectT extends Throwable> implements ExceptionMatcher<SubjectT, SubjectT> {

	private final List<ExceptionMatcher<? extends Throwable, ? extends Throwable>> matchers
			= new LinkedList<ExceptionMatcher<? extends Throwable, ? extends Throwable>>();

	public AndExceptionMatcher() {}

	public void addMatcher(ExceptionMatcher<? extends Throwable, ? extends Throwable> matcher) {
		if(matcher != null)
			matchers.add(matcher);
	}

	public boolean isEmpty() {
		return matchers.isEmpty();
	}

	@Override
	public boolean isExpectedException(Throwable exception) {
		for(ExceptionMatcher<? extends Throwable, ? extends Throwable> matcher : matchers) {
			if(!matcher.isExpectedException(exception))
				return false;
		}
		return true;
	}

	@Override
	public void describeExpectedException(Consumer<String> sink) {
		for(ExceptionMatcher<? extends Throwable, ? extends Throwable> matcher : matchers)
			matcher.describeExpectedException(sink);
	}

	@Override
	public void describe(IndentSink sink) {
		BulletIndentSink bullet = new BulletIndentSink(sink, false);
		sink.append("to satisfy all of the following:", false);
		for(ExceptionMatcher<? extends Throwable, ? extends Throwable> matcher : matchers) {
			bullet.reset();
			matcher.describe(bullet);
		}
	}

}
