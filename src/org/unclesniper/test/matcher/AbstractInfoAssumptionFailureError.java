package org.unclesniper.test.matcher;

import org.unclesniper.test.AssumptionFailureError;

import static org.unclesniper.test.TestUtils.notNull;

public abstract class AbstractInfoAssumptionFailureError extends AssumptionFailureError {

	private final Info info;

	public AbstractInfoAssumptionFailureError(Info info) {
		this(info, null);
	}

	public AbstractInfoAssumptionFailureError(Info info, Throwable cause) {
		super(notNull(info, "Info").makeMessage(), info.makeDescription(), cause);
		this.info = info;
	}

	public Info getInfo() {
		return info;
	}

}
