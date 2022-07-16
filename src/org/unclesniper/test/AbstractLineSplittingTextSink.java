package org.unclesniper.test;

import static org.unclesniper.test.TestUtils.notNull;

public abstract class AbstractLineSplittingTextSink<ExceptionT extends Throwable>
		extends AbstractListStructureTextSink<ExceptionT> {

	private static final char[] EMPTY_CHAR_ARRAY = new char[0];

	private final LineSplitter splitter = new LineSplitter();

	public AbstractLineSplittingTextSink() {}

	protected abstract void putUnbroken(String line) throws ExceptionT;

	protected abstract void putUnbroken(char[] chars, int offset, int length) throws ExceptionT;

	@Override
	protected void putIndentPiece(String piece) throws ExceptionT {
		putUnbroken(piece);
	}

	@Override
	protected void putsUnstructured(String str) throws ExceptionT {
		int length = notNull(str, "String").length();
		int offset = 0;
		while(offset < length) {
			int count = splitter.split(str, offset);
			int end = count < 0 ? length : offset + count;
			if(offset > 0)
				putCurrentIndent();
			if(offset == 0 && end == length)
				putUnbroken(str);
			else
				putUnbroken(str.substring(offset, end));
			offset = count < 0 ? length : end + splitter.getSkip();
		}
		if(splitter.end())
			putUnbroken("");
	}

	@Override
	protected void putchUnstructured(char[] chars, int offset, int length) throws ExceptionT {
		int size = notNull(chars, "Array").length;
		if(offset < 0)
			throw new IllegalArgumentException("Offset must not be negative:" + offset);
		if(length < 0)
			throw new IllegalArgumentException("Length must not be negative: " + length);
		int ubound = offset + length;
		if(ubound > size)
			throw new IllegalArgumentException("Array region end must not exceed array size: "
					+ ubound + " > " + size);
		while(offset < ubound) {
			int count = splitter.split(chars, offset, ubound - offset);
			int end = count < 0 ? ubound : offset + count;
			putUnbroken(chars, offset, end - offset);
			offset = count < 0 ? ubound : end + splitter.getSkip();
		}
		if(splitter.end())
			putUnbroken(AbstractLineSplittingTextSink.EMPTY_CHAR_ARRAY, 0, 0);
	}

}
