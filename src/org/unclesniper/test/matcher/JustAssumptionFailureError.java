package org.unclesniper.test.matcher;

public class JustAssumptionFailureError extends AbstractInfoAssumptionFailureError {

	public JustAssumptionFailureError(JustInfo info) {
		super(info);
	}

	@Override
	public JustInfo getInfo() {
		return (JustInfo)super.getInfo();
	}

}
