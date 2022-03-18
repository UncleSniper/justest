package org.unclesniper.test;

@FunctionalInterface
public interface Testcase<BaseT> {

	void performTest(BaseT base, TestContext context) throws Throwable;

}
