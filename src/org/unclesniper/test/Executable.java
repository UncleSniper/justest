package org.unclesniper.test;

import static org.unclesniper.test.TestUtils.notNull;

@FunctionalInterface
public interface Executable {

	void execute() throws Throwable;

	public static Executable of(Runnable executable) {
		notNull(executable, "Executable");
		return () -> executable.run();
	}

}
