package org.unclesniper.test.matcher;

import java.util.Optional;
import org.unclesniper.test.IndentSink;

import static org.unclesniper.test.TestUtils.notNull;

public class JustMatcher<ElementT> implements Matcher<Optional<ElementT>, ElementT> {

	private final Matcher<? super ElementT, ?> elementMatcher;

	public JustMatcher(Matcher<? super ElementT, ?> elementMatcher) {
		this.elementMatcher = elementMatcher;
	}

	public Matcher<? super ElementT, ?> getElementMatcher() {
		return elementMatcher;
	}

	@Override
	public ElementT match(Optional<ElementT> subject, boolean assume) {
		if(subject != null && subject.isPresent()
				&& (elementMatcher == null || elementMatcher.matches(subject.get())))
			return subject.get();
		JustInfo info = new JustInfo(subject, elementMatcher);
		if(assume)
			throw new JustAssumptionFailureError(info);
		else
			throw new JustAssertionFailureError(info);
	}

	@Override
	public boolean matches(Optional<ElementT> subject) {
		return subject != null && subject.isPresent()
				&& (elementMatcher == null || elementMatcher.matches(subject.get()));
	}

	@Override
	public void describe(IndentSink sink) {
		notNull(sink, "Sink");
		if(elementMatcher == null)
			sink.append("to contain an element", false);
		else {
			sink.append("to contain an element, and that element", false);
			elementMatcher.describe(sink);
		}
	}

}
