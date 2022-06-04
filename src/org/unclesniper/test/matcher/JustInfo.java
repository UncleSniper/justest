package org.unclesniper.test.matcher;

import java.util.Optional;
import org.unclesniper.test.TestUtils;
import org.unclesniper.test.IndentSink;

public class JustInfo extends AbstractInfo {

	private final Optional<?> optional;

	private final Matcher<?, ?> elementMatcher;

	public JustInfo(Optional<?> optional, Matcher<?, ?> elementMatcher) {
		this.optional = optional;
		this.elementMatcher = elementMatcher;
	}

	public Optional<?> getOptional() {
		return optional;
	}

	public Matcher<?, ?> getElementMatcher() {
		return elementMatcher;
	}

	@Override
	protected void make(IndentSink sink) {
		sink.append("Expected", false);
		if(optional == null)
			sink.append("<null>", true);
		else if(elementMatcher == null || !optional.isPresent()) {
			sink.append("Optional.empty()", true);
			sink.append("to contain an element, but did not", false);
		}
		else {
			sink.append("Optional.of(" + TestUtils.describeObject(optional.get()) + ')', true);
			sink.append("to contain an element, and that element", false);
			elementMatcher.describe(sink);
			sink.append("but contained element did not", false);
		}
	}

}
