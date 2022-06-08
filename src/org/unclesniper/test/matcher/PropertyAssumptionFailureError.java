package org.unclesniper.test.matcher;

public class PropertyAssumptionFailureError extends AbstractInfoAssertionFailureError {

	public PropertyAssumptionFailureError(PropertyInfo info) {
		super(info);
	}

	@Override
	public PropertyInfo getInfo() {
		return (PropertyInfo)super.getInfo();
	}

}
