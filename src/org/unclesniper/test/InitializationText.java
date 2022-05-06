package org.unclesniper.test;

import static org.unclesniper.test.TestUtils.notNull;

public final class InitializationText {

	private final String text;

	public InitializationText(String text) {
		this.text = notNull(text, "Text");
	}

	public String getText() {
		return text;
	}

}
