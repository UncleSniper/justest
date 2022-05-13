package org.unclesniper.test;

import static org.unclesniper.test.TestUtils.notNull;

public class PackageFramePredicate implements FramePredicate {

	private String packageName;

	public PackageFramePredicate(String packageName) {
		this.packageName = packageName;
	}

	public PackageFramePredicate(Class<?> clazz) {
		this(PackageFramePredicate.packageOf(clazz));
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public boolean test(StackTraceElement frame, String declaringClass, String packageName,
			String unqualifiedClass, String methodName) {
		if(this.packageName == null)
			throw new IllegalStateException("Package name has not been set");
		if(this.packageName.length() == 0)
			return true;
		if(packageName == null)
			return false;
		return packageName.equals(this.packageName) || packageName.startsWith(this.packageName + '.');
	}

	public static String packageOf(Class<?> clazz) {
		String name = notNull(clazz, "Class").getName();
		int pos = name.lastIndexOf('.');
		return pos < 0 ? "" : name.substring(0, pos);
	}

}
