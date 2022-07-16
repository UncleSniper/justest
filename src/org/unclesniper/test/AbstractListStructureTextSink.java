package org.unclesniper.test;

import java.util.Deque;
import java.util.LinkedList;

import static org.unclesniper.test.TestUtils.notNull;

public abstract class AbstractListStructureTextSink<ExceptionT extends Throwable> implements TextSink<ExceptionT> {

	private enum Structure {

		BULLET_LIST,
		PLAIN_LIST

	}

	private static class StructureLevel {

		final Structure structure;

		boolean inItem;

		boolean hadItem;

		Runnable onNextItemLine;

		final String ifEmpty;

		public StructureLevel(Structure structure, String ifEmpty) {
			this.structure = structure;
			this.ifEmpty = ifEmpty;
		}

	}

	private static class IndentLevel {

		final Structure structure;

		String indentString;

		public IndentLevel(Structure structure, String indentString) {
			this.structure = structure;
			this.indentString = indentString;
		}

	}

	public static final String DEFAULT_INDENT_STRING = "    ";

	private static final int FL_SKIP_BULLET_LISTS = 001;
	private static final int FL_SKIP_PLAIN_LISTS  = 002;

	private int flags;

	private String indentString;

	private int indentLevel;

	private final Deque<IndentLevel> indentStack = new LinkedList<IndentLevel>();

	private final Deque<StructureLevel> structStack = new LinkedList<StructureLevel>();

	private boolean cleanLine = true;

	private int skipDepth;

	public AbstractListStructureTextSink() {}

	public AbstractListStructureTextSink(int flags) {
		this.flags = flags;
	}

	public boolean isSkipBulletLists() {
		return (flags & AbstractListStructureTextSink.FL_SKIP_BULLET_LISTS) != 0;
	}

	public void setSkipBulletLists(boolean skipBulletLists) {
		if(skipBulletLists)
			flags |= AbstractListStructureTextSink.FL_SKIP_BULLET_LISTS;
		else
			flags &= ~AbstractListStructureTextSink.FL_SKIP_BULLET_LISTS;
	}

	public boolean isSkipPlainLists() {
		return (flags & AbstractListStructureTextSink.FL_SKIP_PLAIN_LISTS) != 0;
	}

	public void setSkipPlainLists(boolean skipPlainLists) {
		if(skipPlainLists)
			flags |= AbstractListStructureTextSink.FL_SKIP_PLAIN_LISTS;
		else
			flags &= ~AbstractListStructureTextSink.FL_SKIP_PLAIN_LISTS;
	}

	public void setSkipLists(boolean skipLists) {
		setSkipBulletLists(skipLists);
		setSkipPlainLists(skipLists);
	}

	public String getIndentString() {
		return indentString;
	}

	public void setIndentString(String indentString) {
		this.indentString = indentString;
	}

	protected void putIndentPiece(String piece) throws ExceptionT {
		puts(piece);
	}

	protected final void putCurrentIndent() throws ExceptionT {
		for(IndentLevel level : indentStack)
			putIndentPiece(level.indentString);
	}

	protected final void putCurrentIndentIfClean() throws ExceptionT {
		if(cleanLine)
			putCurrentIndent();
	}

	protected final void announceNextItemLine() {
		for(StructureLevel level : structStack) {
			if(level.onNextItemLine != null)
				level.onNextItemLine.run();
		}
	}

	protected final void announceNextItemLineIfClean() {
		if(cleanLine)
			announceNextItemLine();
	}

	protected abstract void endlnUnstructured() throws ExceptionT;

	protected final void clearedLine() {
		cleanLine = true;
	}

	protected abstract void putsUnstructured(String str) throws ExceptionT;

	protected abstract void putchUnstructured(char[] chars, int offset, int length) throws ExceptionT;

	protected final void taintedLine() {
		cleanLine = false;
	}

	protected final void requireCleanLine(String action) {
		if(!cleanLine)
			throw new IllegalStateException("Cannot " + action + " in the middle of a line");
	}

	protected final void requireMayOutputText() {
		if(structStack.isEmpty())
			return;
		StructureLevel currentStructure = structStack.getLast();
		if(!currentStructure.inItem)
			throw new IllegalStateException("Cannot output text: Currently within "
					+ currentStructure.structure.name() + ", but not within item");
	}

	@Override
	public void puts(String str) throws ExceptionT {
		boolean hasText = str != null && str.length() > 0;
		if(hasText) {
			requireMayOutputText();
			announceNextItemLineIfClean();
		}
		putCurrentIndentIfClean();
		putsUnstructured(str);
		if(hasText)
			cleanLine = false;
	}

	@Override
	public void putch(char[] chars, int offset, int length) throws ExceptionT {
		boolean hasText = length > 0;
		if(hasText) {
			requireMayOutputText();
			announceNextItemLineIfClean();
		}
		putCurrentIndentIfClean();
		putchUnstructured(chars, offset, length);
		if(hasText)
			cleanLine = false;
	}

	@Override
	public void endln() throws ExceptionT {
		endlnUnstructured();
		cleanLine = true;
	}

	@Override
	public void indent(int delta) {
		if(delta > 0) {
			String indentString = this.indentString == null
					? AbstractListStructureTextSink.DEFAULT_INDENT_STRING : this.indentString;
			do
				indentStack.addLast(new IndentLevel(null, indentString));
			while(--delta > 0);
		}
		else if(delta < 0) {
			do {
				if(indentStack.isEmpty())
					throw new IllegalStateException("Attempted to unindent without being indented");
				IndentLevel level = indentStack.getLast();
				if(level.structure != null)
					throw new IllegalStateException("Attempted to unindent across enclosing structure: "
							+ level.structure.name());
				indentStack.removeLast();
			} while(++delta < 0);
		}
	}

	private void beginAnyList(String header, String colon, String ifEmpty, String ifSkipped, String structName,
			int skipMask, Structure structType) throws ExceptionT {
		requireCleanLine("start " + structName);
		StructureLevel currentStructure = structStack.isEmpty() ? null : structStack.getLast();
		if(currentStructure != null && !currentStructure.inItem)
			throw new IllegalStateException("Cannot start " + structName + ": Currently in "
					+ currentStructure.structure.name() + ", but not in item");
		if(header == null && colon != null)
			header = "";
		if(header != null && skipDepth == 0)
			puts(header);
		if((flags & skipMask) != 0) {
			if(skipDepth == 0) {
				if(ifSkipped != null)
					puts(ifSkipped);
				if(ifSkipped != null || header != null)
					endln();
			}
			++skipDepth;
		}
		else {
			if(skipDepth == 0) {
				if(colon != null)
					puts(colon);
				if(header != null)
					endln();
			}
		}
		IndentLevel indentLevel = new IndentLevel(structType, "- ");
		indentStack.addLast(indentLevel);
		structStack.addLast(new StructureLevel(structType, ifEmpty));
	}

	private void beginAnyItem(String structName, Structure structType, String initialIndentString,
			String subsequentIndentString) throws ExceptionT {
		StructureLevel currentStructure = structStack.isEmpty() ? null : structStack.getLast();
		if(currentStructure == null || currentStructure.structure != structType)
			throw new IllegalStateException("Cannot start " + structName + " item: Currently not in "
					+ structType.name() + ", but " + (currentStructure == null ? "not in any structure"
					: "in " + currentStructure.structure.name()));
		if(currentStructure.inItem)
			throw new IllegalStateException("Cannot start " + structName + " item: Already in item");
		assert cleanLine;
		currentStructure.inItem = true;
		assert !indentStack.isEmpty();
		IndentLevel indentLevel = indentStack.getLast();
		assert indentLevel.structure == structType;
		indentLevel.indentString = initialIndentString;
		currentStructure.onNextItemLine = () -> indentLevel.indentString = subsequentIndentString;
	}

	private void endAnyItem(String structName, Structure structType) throws ExceptionT {
		requireCleanLine("end " + structName + " item");
		StructureLevel currentStructure = structStack.isEmpty() ? null : structStack.getLast();
		if(currentStructure == null || currentStructure.structure != structType)
			throw new IllegalStateException("Cannot end " + structName + " item: Currently not in "
					+ structType.name() + ", but " + (currentStructure == null ? "not in any structure"
					: "in " + currentStructure.structure.name()));
		if(!currentStructure.inItem)
			throw new IllegalStateException("Cannot end " + structName + " item: Currently not in item");
		assert !indentStack.isEmpty();
		IndentLevel indentLevel = indentStack.getLast();
		assert indentLevel.structure == structType;
		currentStructure.inItem = false;
		currentStructure.hadItem = true;
		currentStructure.onNextItemLine = null;
	}

	private void endAnyList(String structName, Structure structType) throws ExceptionT {
		StructureLevel currentStructure = structStack.isEmpty() ? null : structStack.getLast();
		if(currentStructure == null || currentStructure.structure != structType)
			throw new IllegalStateException("Cannot end " + structName + " item: Currently not in "
					+ structType.name() + ", but " + (currentStructure == null ? "not in any structure"
					: "in " + currentStructure.structure.name()));
		if(currentStructure.inItem)
			throw new IllegalStateException("Cannot end " + structName + ": Still in item");
		assert !indentStack.isEmpty();
		IndentLevel indentLevel = indentStack.getLast();
		if(indentLevel.structure == null)
			throw new IllegalStateException("Cannot end " + structName + ": Still indented beyond list level");
		assert indentLevel.structure == structType;
		if(!currentStructure.hadItem && currentStructure.ifEmpty != null && skipDepth == 0)
			puts(currentStructure.ifEmpty);
		indentStack.removeLast();
		structStack.removeLast();
	}

	@Override
	public void beginBulletList(String ifEmpty) throws ExceptionT {
		beginBulletList(null, null, ifEmpty, null);
	}

	@Override
	public void beginBulletList(String header, String colon, String ifEmpty, String ifSkipped) throws ExceptionT {
		beginAnyList(header, colon, ifEmpty, ifSkipped, "bullet list",
				AbstractListStructureTextSink.FL_SKIP_BULLET_LISTS, Structure.BULLET_LIST);
	}

	@Override
	public void beginBulletItem() throws ExceptionT {
		beginAnyItem("bullet list", Structure.BULLET_LIST, "- ", "  ");
	}

	@Override
	public void endBulletItem() throws ExceptionT {
		endAnyItem("bullet list", Structure.BULLET_LIST);
	}

	@Override
	public void endBulletList() throws ExceptionT {
		endAnyList("bullet list", Structure.BULLET_LIST);
	}

	@Override
	public void beginPlainList(String ifEmpty) throws ExceptionT {
		beginPlainList(null, null, ifEmpty, null);
	}

	@Override
	public void beginPlainList(String header, String colon, String ifEmpty, String ifSkipped) throws ExceptionT {
		beginAnyList(header, colon, ifEmpty, ifSkipped, "plain list",
				AbstractListStructureTextSink.FL_SKIP_PLAIN_LISTS, Structure.PLAIN_LIST);
	}

	@Override
	public void beginPlainItem() throws ExceptionT {
		String indentString = this.indentString == null
				? AbstractListStructureTextSink.DEFAULT_INDENT_STRING : this.indentString;
		beginAnyItem("plain list", Structure.PLAIN_LIST, indentString, indentString);
	}

	@Override
	public void endPlainItem() throws ExceptionT {
		endAnyItem("plain list", Structure.PLAIN_LIST);
	}

	@Override
	public void endPlainList() throws ExceptionT {
		endAnyList("plain list", Structure.PLAIN_LIST);
	}

}
