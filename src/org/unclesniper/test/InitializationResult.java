package org.unclesniper.test;

public interface InitializationResult extends CapturedOutput {

	Object getBase();

	Throwable getError();

}
