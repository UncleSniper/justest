package org.unclesniper.test;

import java.io.IOException;

public interface TextWriter {

	void puts(String str) throws IOException;

	void putch(char[] chars, int offset, int length) throws IOException;

	void endln() throws IOException;

}
