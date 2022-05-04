package org.unclesniper.test;

public class Assert {

	private Assert() {}

	public static void fail(String message) {
		throw new UnconditionalAssertionFailureError(message);
	}

}
