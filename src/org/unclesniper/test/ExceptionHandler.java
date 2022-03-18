package org.unclesniper.test;

public interface ExceptionHandler<ExceptionT extends Throwable> {

	void handleException(Throwable exception) throws ExceptionT;

}
