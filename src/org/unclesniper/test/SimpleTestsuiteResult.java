package org.unclesniper.test;

import java.util.Collection;
import java.util.stream.Stream;
import java.util.function.Supplier;

public class SimpleTestsuiteResult extends AbstractTestResult implements TestsuiteResult {

	private final Supplier<? extends Stream<? extends TestResult>> containedResults;

	private final boolean forceFail;

	public SimpleTestsuiteResult(String name, Supplier<? extends Stream<? extends TestResult>> containedResults) {
		this(name, containedResults, false);
	}

	public SimpleTestsuiteResult(String name, Collection<? extends TestResult> containedResults) {
		this(name, containedResults, false);
	}

	public SimpleTestsuiteResult(String name, Supplier<? extends Stream<? extends TestResult>> containedResults,
			boolean forceFail) {
		super(name);
		this.containedResults = containedResults;
		this.forceFail = forceFail;
	}

	public SimpleTestsuiteResult(String name, Collection<? extends TestResult> containedResults,
			boolean forceFail) {
		super(name);
		this.containedResults = () -> containedResults == null ? null : containedResults.stream();
		this.forceFail = forceFail;
	}

	public boolean isForceFail() {
		return forceFail;
	}

	@Override
	public TestStatus getStatus() {
		return forceFail ? TestStatus.FAILED : SimpleTestsuiteResult.reduceStatus(getContainedResults());
	}

	@Override
	public Stream<? extends TestResult> getContainedResults() {
		Stream<? extends TestResult> stream = containedResults == null ? null : containedResults.get();
		return stream == null ? Stream.empty() : stream;
	}

	public static TestStatus reduceStatus(Stream<? extends TestResult> containedResults) {
		TestResultStatusReducer reducer = new TestResultStatusReducer();
		if(containedResults != null)
			containedResults.forEach(reducer);
		return reducer.getStatus();
	}

}
