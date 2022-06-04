package org.unclesniper.test.matcher;

public class JustAssertionFailureError extends AbstractInfoAssertionFailureError {

	public JustAssertionFailureError(JustInfo info) {
		super(info);
	}

	@Override
	public JustInfo getInfo() {
		return (JustInfo)super.getInfo();
	}

}
