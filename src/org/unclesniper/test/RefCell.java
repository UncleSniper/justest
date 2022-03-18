package org.unclesniper.test;

public class RefCell<ValueT> {

	private ValueT value;

	public RefCell() {}

	public RefCell(ValueT value) {
		this.value = value;
	}

	public ValueT getValue() {
		return value;
	}

	public void setValue(ValueT value) {
		this.value = value;
	}

}
