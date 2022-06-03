package org.unclesniper.test.matcher;

import java.util.regex.Pattern;
import java.util.function.Consumer;
import org.unclesniper.test.IndentSink;

import static org.unclesniper.test.TestUtils.notNull;

public class MessageExceptionMatcher<SubjectT extends Throwable> implements ExceptionMatcher<SubjectT, SubjectT> {

	public static final String GAP_INDICATOR = "<...>";

	private final String message;

	private final Pattern pattern;

	public MessageExceptionMatcher(String message) {
		this.message = message;
		pattern = MessageExceptionMatcher.makePattern(message);
	}

	public String getMessage() {
		return message;
	}

	@Override
	public boolean isExpectedException(Throwable exception) {
		if(exception == null)
			return false;
		String have = exception.getMessage();
		if(have == null)
			return message == null;
		if(message == null)
			return false;
		return pattern.matcher(message).matches();
	}

	@Override
	public void describeExpectedException(Consumer<String> sink) {
		notNull(sink, "Sink");
		if(message == null)
			sink.accept("- with null message");
		else {
			sink.accept("- with message:");
			sink.accept("    " + message);
		}
	}

	@Override
	public void describe(IndentSink sink) {
		notNull(sink, "Sink");
		if(message == null)
			sink.append("to have a null message", false);
		else {
			sink.append("to have message:", false);
			sink.append(message, true);
		}
	}

	private static Pattern makePattern(String message) {
		if(message == null)
			return null;
		StringBuilder builder = new StringBuilder();
		int old = 0;
		final int gapLength = MessageExceptionMatcher.GAP_INDICATOR.length();
		for(;;) {
			int pos = message.indexOf(MessageExceptionMatcher.GAP_INDICATOR, old);
			if(pos < 0)
				break;
			if(pos > old)
				builder.append(Pattern.quote(message.substring(old, pos)));
			builder.append(".*");
			old = pos + gapLength;
		}
		if(old < message.length())
			builder.append(Pattern.quote(message.substring(old)));
		return Pattern.compile(builder.toString());
	}

}
