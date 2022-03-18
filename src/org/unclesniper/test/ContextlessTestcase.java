package org.unclesniper.test;

@FunctionalInterface
public interface ContextlessTestcase<BaseT> {

	void performTest(BaseT base) throws Throwable;

}
