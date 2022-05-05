package org.unclesniper.test;

public class PackageFramePredicate implements FramePredicate {

	private String packageName;

	public PackageFramePredicate(String packageName) {
		this.packageName = packageName;
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
		if(clazz == null)
			throw new IllegalArgumentException("Class must not be null");
		String name = clazz.getName();
		int pos = name.lastIndexOf('.');
		return pos < 0 ? "" : name.substring(0, pos);
	}

}
