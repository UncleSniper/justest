package org.unclesniper.test;

public interface ContextInitializer<BaseT> {

	void initializeBase(TestContext context, BaseT base) throws Throwable;

}
