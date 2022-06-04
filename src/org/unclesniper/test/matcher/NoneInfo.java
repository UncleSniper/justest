package org.unclesniper.test.matcher;

import java.util.Optional;
import org.unclesniper.test.TestUtils;
import org.unclesniper.test.IndentSink;

public class NoneInfo extends AbstractInfo {

	private final Optional<?> optional;

	public NoneInfo(Optional<?> optional) {
		this.optional = optional;
	}

	public Optional<?> getOptional() {
		return optional;
	}

	@Override
	protected void make(IndentSink sink) {
		sink.append("Expected", false);
		if(optional == null)
			sink.append("<null>", true);
		else
			sink.append("Optional.of(" + TestUtils.describeObject(optional.get()) + ')', true);
		sink.append(optional == null ? "to be empty, but was null" : "to be empty, but was not", false);
	}

}
