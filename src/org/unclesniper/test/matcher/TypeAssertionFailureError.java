package org.unclesniper.test.matcher;

public class TypeAssertionFailureError extends AbstractInfoAssertionFailureError {

	public TypeAssertionFailureError(TypeInfo info) {
		super(info);
	}

	@Override
	public TypeInfo getInfo() {
		return (TypeInfo)super.getInfo();
	}

}
