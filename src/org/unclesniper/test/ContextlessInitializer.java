package org.unclesniper.test;

public interface ContextlessInitializer<BaseT> {

	void initializeBase(BaseT base) throws Throwable;

}
