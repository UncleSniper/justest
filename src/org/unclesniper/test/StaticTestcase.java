package org.unclesniper.test;

@FunctionalInterface
public interface StaticTestcase {

	void performTest(TestContext context) throws Throwable;

}
