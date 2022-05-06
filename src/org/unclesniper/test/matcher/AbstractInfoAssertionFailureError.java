package org.unclesniper.test.matcher;

import org.unclesniper.test.AssertionFailureError;

import static org.unclesniper.test.TestUtils.notNull;

public abstract class AbstractInfoAssertionFailureError extends AssertionFailureError {

	private final Info info;

	public AbstractInfoAssertionFailureError(Info info) {
		super(notNull(info, "Info").makeMessage(), info.makeDescription());
		this.info = info;
	}

	public Info getInfo() {
		return info;
	}

}
