package org.unclesniper.test;

import static org.unclesniper.test.TestUtils.notNull;

public class LineSplitter {

	private interface CharGetter {

		char charAt(int index);

	}

	private boolean hadCarriageReturn;

	private int skip;

	public LineSplitter() {}

	public int getSkip() {
		return skip;
	}

	private int split(CharGetter str, int start, int end) {
		for(int i = start; i < end; ++i) {
			char c = str.charAt(i);
			if(hadCarriageReturn) {
				hadCarriageReturn = false;
				int notAtStart = i > start ? 1 : 0;
				skip = (c == '\n' ? 1 : 0) + notAtStart;
				return i - start - notAtStart;
			}
			switch(c) {
				case '\r':
					hadCarriageReturn = true;
				default:
					break;
				case '\n':
					skip = 1;
					return i - start;
			}
		}
		if(hadCarriageReturn && start < end) {
			skip = 1;
			return end - start - 1;
		}
		return -1;
	}

	public int split(String str, int offset) {
		if(offset < 0)
			throw new IllegalArgumentException("Offset must not be negative: " + offset);
		int length = notNull(str, "String to split").length();
		if(offset > length)
			throw new IllegalArgumentException("Offset must not exceed length of string: "
					+ offset + " > " + length);
		return split(str::charAt, offset, length);
	}

	public int split(char[] chars, int offset, int length) {
		if(offset < 0)
			throw new IllegalArgumentException("Offset must not be negative: " + offset);
		if(length < 0)
			throw new IllegalArgumentException("Character count must not be negative: " + length);
		int size = notNull(chars, "Character array to split").length;
		int end = offset + length;
		if(end > size)
			throw new IllegalArgumentException("End of range must not exceed array size: " + end + " > " + size);
		return split(index -> chars[index], offset, end);
	}

	public boolean end() {
		if(!hadCarriageReturn)
			return false;
		hadCarriageReturn = false;
		return true;
	}

}
