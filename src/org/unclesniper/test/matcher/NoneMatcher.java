package org.unclesniper.test.matcher;

import java.util.Optional;
import org.unclesniper.test.IndentSink;

import static org.unclesniper.test.TestUtils.notNull;

public class NoneMatcher<ElementT> implements Matcher<Optional<ElementT>, Optional<ElementT>> {

	public NoneMatcher() {}

	@Override
	public Optional<ElementT> match(Optional<ElementT> subject, boolean assume) {
		if(subject != null && !subject.isPresent())
			return subject;
		NoneInfo info = new NoneInfo(subject);
		if(assume)
			throw new NoneAssumptionFailureError(info);
		else
			throw new NoneAssertionFailureError(info);
	}

	@Override
	public boolean matches(Optional<ElementT> subject) {
		return subject != null && !subject.isPresent();
	}

	@Override
	public void describe(IndentSink sink) {
		notNull(sink, "Sink");
		sink.append("to be empty", false);
	}

}
