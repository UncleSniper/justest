package org.unclesniper.test;

public interface FramePredicate {

	boolean test(StackTraceElement frame, String declaringClass, String packageName, String unqualifiedClass,
			String methodName);

}
