package org.unclesniper.test;

public abstract class AbstractTestResult implements TestResult {

	private final String name;

	public AbstractTestResult(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

}
