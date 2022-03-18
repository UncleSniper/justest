package org.unclesniper.test;

public interface Action<ExceptionT extends Throwable> {

	void perform() throws ExceptionT;

}
