package org.unclesniper.test;

import java.util.List;
import java.util.Deque;
import java.util.Objects;
import java.io.IOException;
import java.util.LinkedList;
import java.util.stream.Stream;
import java.util.function.Function;
import org.unclesniper.test.resource.Localization;

public abstract class AbstractTextTestResultSink implements TestResultSink {

	private static final class Frame {

		final String name;

		final boolean suite;

		final boolean deferred;

		final List<InitFailure> levelInitFailures;

		Frame(String name, boolean suite, boolean deferred, List<InitFailure> levelInitFailures) {
			this.name = name;
			this.suite = suite;
			this.deferred = deferred;
			this.levelInitFailures = levelInitFailures;
		}

	}

	private static final class Failure {

		final String path;

		final TestcaseResult result;

		Failure(String path, TestcaseResult result) {
			this.path = path;
			this.result = result;
		}

	}

	private static final class InitFailure {

		final String path;

		final InitializationResult result;

		final boolean required;

		final boolean finalizing;

		InitFailure(String path, InitializationResult result, boolean required, boolean finalizing) {
			this.path = path;
			this.result = result;
			this.required = required;
			this.finalizing = finalizing;
		}

	}

	private final class ThisTextWriter implements TextWriter {

		int indentLevel;

		String lineInitiator;

		boolean dropLineInitiator;

		String banner;

		boolean cleanLine = true;

		ThisTextWriter() {}

		void clearLine() throws IOException {
			if(!cleanLine)
				endln();
		}

		void reset() {
			indentLevel = 0;
			lineInitiator = banner = null;
			dropLineInitiator = false;
			cleanLine = true;
		}

		private void prepareLine() throws IOException {
			if(!cleanLine)
				return;
			if(banner != null) {
				indent(indentLevel);
				AbstractTextTestResultSink.this.puts(banner);
				AbstractTextTestResultSink.this.endln();
			}
			indent(indentLevel);
			if(lineInitiator != null) {
				AbstractTextTestResultSink.this.puts(lineInitiator);
				if(dropLineInitiator)
					lineInitiator = null;
			}
			cleanLine = false;
		}

		@Override
		public void puts(String str) throws IOException {
			if(str == null || str.length() == 0)
				return;
			prepareLine();
			AbstractTextTestResultSink.this.puts(str);
		}

		@Override
		public void putch(char[] chars, int offset, int length) throws IOException {
			if(length <= 0)
				return;
			prepareLine();
			AbstractTextTestResultSink.this.putch(chars, offset, length);
		}

		@Override
		public void endln() throws IOException {
			AbstractTextTestResultSink.this.endln();
			cleanLine = true;
		}

	}

	public static final String DEFAULT_INDENT = "    ";

	private static final char[] WIDE_SPACES = "                                                       ".toCharArray();

	private static final int WIDE_SPACE_WIDTH = WIDE_SPACES.length;

	protected static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static final String L10N_PREFIX = AbstractTextTestResultSink.class.getName() + '.';

	private static final String ANONYMOUS_MSG = Localization.DEFAULT.getMessage(L10N_PREFIX + "anonymous");

	private static final String SUMMARY_MSG = Localization.DEFAULT.getMessage(L10N_PREFIX + "summary");

	private static final String TEST_SUITES_MSG = Localization.DEFAULT.getMessage(L10N_PREFIX + "testSuites");

	private static final String PASSED_COUNT_MSG = Localization.DEFAULT.getMessage(L10N_PREFIX + "passedCount");

	private static final String SKIPPED_COUNT_MSG = Localization.DEFAULT.getMessage(L10N_PREFIX + "skippedCount");

	private static final String FAILED_COUNT_MSG = Localization.DEFAULT.getMessage(L10N_PREFIX + "failedCount");

	private static final String TESTCASES_MSG = Localization.DEFAULT.getMessage(L10N_PREFIX + "testcases");

	private static final String STDOUT_MSG = Localization.DEFAULT.getMessage(L10N_PREFIX + "stdOut");

	private static final String STDERR_MSG = Localization.DEFAULT.getMessage(L10N_PREFIX + "stdErr");

	private String indentString;

	private final Deque<Frame> stack = new LinkedList<Frame>();

	private int currentLevel;

	private boolean showSkipped;

	private boolean showSkippedDetails;

	private boolean showPassed;

	private final List<Failure> failures = new LinkedList<Failure>();

	private final List<Failure> skips = new LinkedList<Failure>();

	private int passedCases;

	private int skippedCases;

	private int failedCases;

	private int passedSuites;

	private int skippedSuites;

	private int failedSuites;

	private final List<InitFailure> initFailures = new LinkedList<InitFailure>();

	private List<InitFailure> levelInitFailures = new LinkedList<InitFailure>();

	private final ThisTextWriter thisTextWriter = new ThisTextWriter();

	public AbstractTextTestResultSink() {}

	public String getIndentString() {
		return indentString;
	}

	public void setIndentString(String indentString) {
		this.indentString = indentString;
	}

	public boolean isShowSkipped() {
		return showSkipped;
	}

	public void setShowSkipped(boolean showSkipped) {
		this.showSkipped = showSkipped;
	}

	public boolean isShowSkippedDetails() {
		return showSkippedDetails;
	}

	public void setShowSkippedDetails(boolean showSkippedDetails) {
		this.showSkippedDetails = showSkippedDetails;
	}

	public boolean isShowPassed() {
		return showPassed;
	}

	public void setShowPassed(boolean showPassed) {
		this.showPassed = showPassed;
	}

	public String getCurrentTestPath() {
		StringBuilder builder = null;
		for(Frame frame : stack) {
			if(frame.name != null) {
				if(builder == null)
					builder = new StringBuilder();
				else if(builder.length() > 0)
					builder.append(" > ");
				builder.append(frame.name);
			}
		}
		return builder == null ? null : builder.toString();
	}

	public int getPassedCases() {
		return passedCases;
	}

	public int getSkippedCases() {
		return skippedCases;
	}

	public int getFailedCases() {
		return failedCases;
	}

	public int getPassedSuites() {
		return passedSuites;
	}

	public int getSkippedSuites() {
		return skippedSuites;
	}

	public int getFailedSuites() {
		return failedSuites;
	}

	protected final void indent(int level) throws IOException {
		String is = indentString == null ? AbstractTextTestResultSink.DEFAULT_INDENT : indentString;
		for(; level > 0; --level)
			puts(is);
	}

	protected final void indentColumns(int columns) throws IOException {
		for(; columns >= AbstractTextTestResultSink.WIDE_SPACE_WIDTH;
				columns -= AbstractTextTestResultSink.WIDE_SPACE_WIDTH)
			putch(AbstractTextTestResultSink.WIDE_SPACES, 0, AbstractTextTestResultSink.WIDE_SPACE_WIDTH);
		if(columns > 0)
			putch(AbstractTextTestResultSink.WIDE_SPACES, 0, columns);
	}

	protected abstract void puts(String str) throws IOException;

	protected abstract void putch(char[] chars, int offset, int length) throws IOException;

	protected void endln() throws IOException {
		puts(AbstractTextTestResultSink.LINE_SEPARATOR);
	}

	protected void flush() throws IOException {}

	private static String name(String name) {
		return name == null ? AbstractTextTestResultSink.ANONYMOUS_MSG : name;
	}

	private void badNamePop(String callName, String topName) {
		// We're doomed anyway, might just as well blow a few CPU cycles on finding out why
		if(Objects.equals(callName, topName))
			throw new Error("Wrong! Wrong, wrong, wrong! Names in begin*() and end*() must be the same reference");
		throw new IllegalStateException("Expected testcase name " + (topName == null ? "<null>" : '\'' + topName
				+ '\'') + " to be popped, but name " + (callName == null ? "<null>" : '\'' + callName + '\'')
				+ " was attempted to be popped");
	}

	private boolean shouldShow(TestStatus status) {
		switch(status) {
			case PASSED:
				return showPassed;
			case SKIPPED:
				return showSkipped;
			case FAILED:
				return true;
			default:
				throw new Error("Unrecognized TestStatus: " + status.name());
		}
	}

	private void countSuite(TestStatus status) {
		switch(status) {
			case PASSED:
				++passedSuites;
				break;
			case SKIPPED:
				++skippedSuites;
				break;
			case FAILED:
				++failedSuites;
				break;
			default:
				throw new Error("Unrecognized TestStatus: " + status.name());
		}
	}

	private void countCase(TestStatus status) {
		switch(status) {
			case PASSED:
				++passedCases;
				break;
			case SKIPPED:
				++skippedCases;
				break;
			case FAILED:
				++failedCases;
				break;
			default:
				throw new Error("Unrecognized TestStatus: " + status.name());
		}
	}

	private void initFinalResult(String name, InitializationResult result, boolean required, boolean finalizing) {
		if(result == null)
			throw new IllegalArgumentException((finalizing ? "Finalization" : "Initialization")
					+ " result must not be null");
		Throwable error = result.getError();
		if(error == null)
			return;
		InitFailure failure = new InitFailure(getCurrentTestPath(), result, required, finalizing);
		initFailures.add(failure);
		levelInitFailures.add(failure);
	}

	private void flushLevelInitFinalResult(List<InitFailure> levelInitFailures) throws IOException {
		for(InitFailure failure : levelInitFailures) {
			StringBuilder builder = new StringBuilder("Error ");
			if(failure.finalizing)
				builder.append("finalizing");
			else
				builder.append("initializing");
			boolean needBlank = true;
			if(failure.path != null) {
				builder.append(' ');
				builder.append(failure.path);
				builder.append(" [");
				needBlank = false;
			}
			Object base = failure.result.getBase();
			if(base == null)
				;
			else if(base instanceof InitializationText) {
				if(needBlank)
					builder.append(' ');
				builder.append(((InitializationText)base).getText());
			}
			else {
				builder.append(needBlank ? " object " : "object ");
				String str = base.toString();
				if(str != null) {
					builder.append('\'');
					builder.append(str);
					builder.append("' ");
				}
				builder.append("of type ");
				builder.append(base.getClass().getName());
			}
			if(failure.path != null)
				builder.append(']');
			builder.append(": ");
			builder.append(failure.result.getError().toString());
			indent(currentLevel);
			puts(builder.toString());
			endln();
		}
		levelInitFailures.clear();
	}

	@Override
	public void beginRun() throws IOException {}

	@Override
	public void beginTestcase(String name) throws IOException {
		if(showSkipped && showPassed) {
			indent(currentLevel);
			puts(name == null ? AbstractTextTestResultSink.ANONYMOUS_MSG : name);
			flush();
			++currentLevel;
			stack.addLast(new Frame(name, false, false, levelInitFailures));
		}
		else
			stack.addLast(new Frame(name, false, true, levelInitFailures));
		levelInitFailures = new LinkedList<InitFailure>();
	}

	@Override
	public void endTestcase(String name, TestcaseResult result) throws IOException {
		if(result == null)
			throw new IllegalArgumentException("Testcase result must not be null");
		if(stack.isEmpty())
			throw new IllegalStateException("Stack underflow");
		String path = getCurrentTestPath();
		Frame top = stack.removeLast();
		levelInitFailures = top.levelInitFailures;
		if(top.suite)
			throw new IllegalStateException("Sequencing error: Expected endTestsuite(), but endTestcase() called");
		if(name != top.name)
			badNamePop(name, top.name);
		TestStatus status = result.getStatus();
		if(status == null)
			status = TestStatus.PASSED;
		countCase(status);
		switch(status) {
			case FAILED:
				failures.add(new Failure(path, result));
				break;
			case SKIPPED:
				skips.add(new Failure(path, result));
				break;
		}
		boolean shouldShow = shouldShow(status);
		if(top.deferred) {
			if(shouldShow) {
				indent(currentLevel);
				puts(name == null ? AbstractTextTestResultSink.ANONYMOUS_MSG : name);
			}
		}
		else
			--currentLevel;
		if(shouldShow) {
			puts(": ");
			puts(status.getLocalizedName());
			endln();
		}
		flushLevelInitFinalResult(top.levelInitFailures);
		flush();
	}

	@Override
	public void beginTestsuite(String name) throws IOException {
		if(name != null) {
			indent(currentLevel);
			puts(name);
			puts(" {");
			endln();
			flush();
			++currentLevel;
		}
		stack.addLast(new Frame(name, true, false, levelInitFailures));
		levelInitFailures = new LinkedList<InitFailure>();
	}

	@Override
	public void endTestsuite(String name, TestsuiteResult result) throws IOException {
		if(result == null)
			throw new IllegalArgumentException("Testsuite result must not be null");
		if(stack.isEmpty())
			throw new IllegalStateException("Stack underflow");
		Frame top = stack.removeLast();
		levelInitFailures = top.levelInitFailures;
		if(!top.suite)
			throw new IllegalStateException("Sequencing error: Expected endTestcase(), but endTestsuite() called");
		if(name != top.name)
			badNamePop(name, top.name);
		TestStatus status = result.getStatus();
		if(status == null)
			status = TestStatus.PASSED;
		countSuite(status);
		if(name != null) {
			--currentLevel;
			indent(currentLevel);
			puts("} ");
			puts(name);
			puts(": ");
			puts(status.getLocalizedName());
			endln();
		}
		flushLevelInitFinalResult(top.levelInitFailures);
		flush();
	}

	@Override
	public void initializationResult(String name, InitializationResult result, boolean required)
			throws IOException {
		initFinalResult(name, result, required, false);
	}

	@Override
	public void finalizationResult(String name, InitializationResult result, boolean required) throws IOException {
		initFinalResult(name, result, required, true);
	}

	private void printOutput(Stream<String> output, String banner) throws IOException {
		if(output == null)
			return;
		thisTextWriter.banner = banner;
		thisTextWriter.lineInitiator = "|| ";
		thisTextWriter.indentLevel = 3;
		TestUtils.printStream(output, thisTextWriter);
		thisTextWriter.clearLine();
		thisTextWriter.reset();
	}

	private void printFailure(Failure failure, Function<TestcaseResult, Throwable> getException)
			throws IOException {
		indent(2);
		puts(failure.path == null ? AbstractTextTestResultSink.ANONYMOUS_MSG : failure.path);
		Throwable error = getException.apply(failure.result);
		if(error == null)
			endln();
		else {
			puts(": ");
			thisTextWriter.indentLevel = 3;
			thisTextWriter.cleanLine = false;
			TestUtils.printStackTrace(error, thisTextWriter);
			thisTextWriter.clearLine();
			thisTextWriter.reset();
		}
		printOutput(failure.result.getStandardOutput(), AbstractTextTestResultSink.STDOUT_MSG);
		printOutput(failure.result.getStandardError(), AbstractTextTestResultSink.STDERR_MSG);
	}

	@Override
	public void endRun() throws IOException {
		if(!stack.isEmpty())
			throw new IllegalStateException("Stack should be empty, but is not");
		flushLevelInitFinalResult(levelInitFailures);
		int caseCount = passedCases + skippedCases + failedCases;
		int suiteCount = passedSuites + skippedSuites + failedSuites;
		int effectiveCaseCount = (showPassed ? passedCases : 0) + (showSkipped ? skippedCases : 0) + failedCases;
		int effectiveSuiteCount = (showPassed ? passedSuites : 0) + (showSkipped ? skippedSuites : 0) + failedSuites;
		if(effectiveSuiteCount == 0 && effectiveCaseCount == 0)
			return;
		endln();
		puts("==== ");
		puts(AbstractTextTestResultSink.SUMMARY_MSG);
		puts(" ====");
		endln();
		if(effectiveSuiteCount > 0) {
			puts(AbstractTextTestResultSink.TEST_SUITES_MSG + ':');
			endln();
			int width = TestUtils.digitCount(suiteCount, false);
			if(showPassed && passedSuites > 0) {
				indent(1);
				puts(TestUtils.padLeft(String.valueOf(passedSuites), ' ', width));
				puts('/' + String.valueOf(suiteCount));
				puts(' ' + AbstractTextTestResultSink.PASSED_COUNT_MSG);
				endln();
			}
			if(showSkipped && skippedSuites > 0) {
				indent(1);
				puts(TestUtils.padLeft(String.valueOf(skippedSuites), ' ', width));
				puts('/' + String.valueOf(suiteCount));
				puts(' ' + AbstractTextTestResultSink.SKIPPED_COUNT_MSG);
				endln();
			}
			if(failedSuites > 0) {
				indent(1);
				puts(TestUtils.padLeft(String.valueOf(failedSuites), ' ', width));
				puts('/' + String.valueOf(suiteCount));
				puts(' ' + AbstractTextTestResultSink.FAILED_COUNT_MSG);
				endln();
			}
		}
		if(effectiveCaseCount > 0) {
			puts(AbstractTextTestResultSink.TESTCASES_MSG + ':');
			endln();
			int width = TestUtils.digitCount(caseCount, false);
			if(showPassed && passedCases > 0) {
				indent(1);
				puts(TestUtils.padLeft(String.valueOf(passedCases), ' ', width));
				puts('/' + String.valueOf(caseCount));
				puts(' ' + AbstractTextTestResultSink.PASSED_COUNT_MSG);
				endln();
			}
			if(showSkipped && skippedCases > 0) {
				indent(1);
				puts(TestUtils.padLeft(String.valueOf(skippedCases), ' ', width));
				puts('/' + String.valueOf(caseCount));
				puts(' ' + AbstractTextTestResultSink.SKIPPED_COUNT_MSG + (showSkippedDetails ? ":" : ""));
				endln();
				if(showSkippedDetails) {
					for(Failure skip : skips)
						printFailure(skip, TestcaseResult::getAssumptionFailureError);
				}
			}
			if(failedCases > 0) {
				indent(1);
				puts(TestUtils.padLeft(String.valueOf(failedCases), ' ', width));
				puts('/' + String.valueOf(caseCount));
				puts(' ' + AbstractTextTestResultSink.FAILED_COUNT_MSG + ':');
				endln();
				for(Failure failure : failures)
					printFailure(failure, TestcaseResult::getAssertionFailureError);
			}
		}
	}

}
