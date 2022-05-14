package org.unclesniper.test.matcher;

public class TypeAssumptionFailureError extends AbstractInfoAssumptionFailureError {

	public TypeAssumptionFailureError(TypeInfo info) {
		super(info);
	}

	@Override
	public TypeInfo getInfo() {
		return (TypeInfo)super.getInfo();
	}

}
