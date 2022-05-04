package org.unclesniper.test;

import java.util.List;
import java.io.IOException;
import java.util.LinkedList;

public abstract class AbstractTestsuite<BaseT> implements Testable<BaseT> {

	private static class ObservingTestResultSink implements TestResultSink {

		private final List<TestResult> results = new LinkedList<TestResult>();

		private final TestResultSink slave;

		private int level;

		ObservingTestResultSink(TestResultSink slave) {
			this.slave = slave;
		}

		List<TestResult> getResults() {
			return results;
		}

		@Override
		public void beginRun() throws IOException {
			if(slave != null)
				slave.beginRun();
		}

		@Override
		public void beginTestcase(String name) throws IOException {
			if(level >= 0)
				++level;
			if(slave != null)
				slave.beginTestcase(name);
		}

		@Override
		public void endTestcase(String name, TestcaseResult result) throws IOException {
			if(level >= 0)
				--level;
			if(level == 0 && result != null)
				results.add(result);
			if(slave != null)
				slave.endTestcase(name, result);
		}

		@Override
		public void beginTestsuite(String name) throws IOException {
			if(level >= 0)
				++level;
			if(slave != null)
				slave.beginTestsuite(name);
		}

		@Override
		public void endTestsuite(String name, TestsuiteResult result) throws IOException {
			if(level >= 0)
				--level;
			if(level == 0 && result != null)
				results.add(result);
			if(slave != null)
				slave.endTestsuite(name, result);
		}

		@Override
		public void initializationResult(String name, InitializationResult result, boolean required)
				throws IOException {
			if(slave != null)
				slave.initializationResult(name, result, required);
		}

		@Override
		public void finalizationResult(String name, InitializationResult result, boolean required)
				throws IOException {
			if(slave != null)
				slave.finalizationResult(name, result, required);
		}

		@Override
		public void endRun() throws IOException {
			if(slave != null)
				slave.endRun();
		}

	}

	private final String name;

	public AbstractTestsuite(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	protected abstract boolean performSuite(TestContext context, BaseT base, TestResultSink sink) throws IOException;

	@Override
	public void performTest(TestContext context, BaseT base, TestResultSink sink) throws IOException {
		if(sink == null) {
			performSuite(context, base, sink);
			return;
		}
		sink.beginTestsuite(name);
		ObservingTestResultSink observer = new ObservingTestResultSink(sink);
		boolean forceFail = performSuite(context, base, observer);
		sink.endTestsuite(name, new SimpleTestsuiteResult(name, observer.getResults(), forceFail));
	}

}
