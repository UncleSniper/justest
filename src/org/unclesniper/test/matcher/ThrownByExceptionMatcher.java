package org.unclesniper.test.matcher;

import java.util.function.Consumer;

import static org.unclesniper.test.TestUtils.notNull;

public class ThrownByExceptionMatcher<SubjectT extends Throwable> implements ExceptionMatcher<SubjectT, SubjectT> {

	private final String className;

	private final String methodName;

	private final boolean inward;

	public ThrownByExceptionMatcher(String className) {
		this(className, null, false);
	}

	public ThrownByExceptionMatcher(String className, boolean inward) {
		this(className, null, inward);
	}

	public ThrownByExceptionMatcher(String className, String methodName) {
		this(className, methodName, false);
	}

	public ThrownByExceptionMatcher(String className, String methodName, boolean inward) {
		this.className = notNull(className, "Class name");
		this.methodName = methodName;
		this.inward = inward;
	}

	public ThrownByExceptionMatcher(Class<?> clazz) {
		this(clazz, null, false);
	}

	public ThrownByExceptionMatcher(Class<?> clazz, boolean inward) {
		this(clazz, null, inward);
	}

	public ThrownByExceptionMatcher(Class<?> clazz, String methodName) {
		this(clazz, methodName, false);
	}

	public ThrownByExceptionMatcher(Class<?> clazz, String methodName, boolean inward) {
		this.className = notNull(clazz, "Class").getName();
		this.methodName = methodName;
		this.inward = inward;
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}

	public boolean isInward() {
		return inward;
	}

	@Override
	public boolean isExpectedException(Throwable exception) {
		if(exception == null)
			return false;
		StackTraceElement[] trace = exception.getStackTrace();
		for(StackTraceElement frame : trace) {
			if(className.equals(frame.getClassName())
					&& (methodName == null || methodName.equals(frame.getMethodName())))
				return true;
			if(!inward)
				return false;
		}
		return false;
	}

	@Override
	public void describeExpectedException(Consumer<String> sink) {
		notNull(sink, "Sink").accept("- thrown " + (inward ? "somewhere" : "directly") + " in:");
		sink.accept("    class " + className + (methodName == null ? "" : ", method " + methodName));
	}

}
