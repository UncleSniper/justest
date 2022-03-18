package org.unclesniper.test;

public class EmptyTestContext implements TestContext {

	public static final TestContext instance = new EmptyTestContext();

	public EmptyTestContext() {}

	@Override
	public Object getContextObject(String key) {
		return null;
	}

	@Override
	public String getContextParameter(String name) {
		return null;
	}

}
