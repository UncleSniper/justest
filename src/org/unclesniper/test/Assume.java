package org.unclesniper.test;

public class Assume {

	private Assume() {}

	public static void skip(String message) {
		throw new UnconditionalAssumptionFailureError(message);
	}

}
