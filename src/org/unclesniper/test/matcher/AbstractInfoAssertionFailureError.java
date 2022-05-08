package org.unclesniper.test.matcher;

import org.unclesniper.test.AssertionFailureError;

import static org.unclesniper.test.TestUtils.notNull;

public abstract class AbstractInfoAssertionFailureError extends AssertionFailureError {

	private final Info info;

	public AbstractInfoAssertionFailureError(Info info) {
		this(info, null);
	}

	public AbstractInfoAssertionFailureError(Info info, Throwable cause) {
		super(notNull(info, "Info").makeMessage(), info.makeDescription(), cause);
		this.info = info;
	}

	public Info getInfo() {
		return info;
	}

}
