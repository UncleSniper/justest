package org.unclesniper.test;

public interface TextSink<ExceptionT extends Throwable> {

	void puts(String str) throws ExceptionT;

	void putch(char[] chars, int offset, int length) throws ExceptionT;

	void endln() throws ExceptionT;

	void indent(int delta);

	void beginBulletList(String ifEmpty) throws ExceptionT;

	void beginBulletList(String header, String colon, String ifEmpty, String ifSkipped) throws ExceptionT;

	void beginBulletItem() throws ExceptionT;

	void endBulletItem() throws ExceptionT;

	void endBulletList() throws ExceptionT;

	void beginPlainList(String ifEmpty) throws ExceptionT;

	void beginPlainList(String header, String colon, String ifEmpty, String ifSkipped) throws ExceptionT;

	void beginPlainItem() throws ExceptionT;

	void endPlainItem() throws ExceptionT;

	void endPlainList() throws ExceptionT;

	default void putln(int indent, String str) throws ExceptionT {
		this.indent(indent);
		this.puts(str);
		this.endln();
		this.indent(-indent);
	}

	default void putln(String str) throws ExceptionT {
		this.puts(str);
		this.endln();
	}

}
