package org.unclesniper.test.matcher;

public class PropertyAssertionFailureError extends AbstractInfoAssertionFailureError {

	public PropertyAssertionFailureError(PropertyInfo info) {
		super(info);
	}

	@Override
	public PropertyInfo getInfo() {
		return (PropertyInfo)super.getInfo();
	}

}
