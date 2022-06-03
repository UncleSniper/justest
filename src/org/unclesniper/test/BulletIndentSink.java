package org.unclesniper.test;

import static org.unclesniper.test.TestUtils.notNull;

public class BulletIndentSink implements IndentSink {

	private final IndentSink sink;

	private final boolean outerIndent;

	private boolean hadLine;

	public BulletIndentSink(IndentSink sink, boolean outerIndent) {
		this.sink = notNull(sink, "Sink");
		this.outerIndent = outerIndent;
	}

	public IndentSink getSink() {
		return sink;
	}

	public boolean isOuterIndent() {
		return outerIndent;
	}

	public void reset() {
		hadLine = false;
	}

	@Override
	public void append(String line, boolean innerIndent) {
		if(line == null) {
			sink.append(null, outerIndent);
			return;
		}
		if(innerIndent)
			line = "    " + line;
		if(hadLine)
			sink.append("  " + line, outerIndent);
		else {
			sink.append("- " + line, outerIndent);
			hadLine = true;
		}
	}

}
