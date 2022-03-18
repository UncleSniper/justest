package org.unclesniper.test;

public final class InitializationText {

	private final String text;

	public InitializationText(String text) {
		if(text == null)
			throw new IllegalArgumentException("Text must not be null");
		this.text = text;
	}

	public String getText() {
		return text;
	}

}
