package org.unclesniper.test;

import java.io.IOException;
import java.util.function.Predicate;

public abstract class AbstractTestcase<BaseT> implements Testable<BaseT> {

	private final String name;

	private final Predicate<? super Throwable> expectedException;

	private final String expectedExceptionName;

	public AbstractTestcase(String name, Predicate<? super Throwable> expectedException,
			String expectedExceptionName) {
		this.name = name;
		this.expectedException = expectedException;
		this.expectedExceptionName = expectedExceptionName;
	}

	public String getName() {
		return name;
	}

	public Predicate<? super Throwable> getExpectedException() {
		return expectedException;
	}

	public String getExpectedExceptionName() {
		return expectedExceptionName;
	}

	protected abstract void performTest(TestContext context, BaseT base) throws Throwable;

	@Override
	public void performTest(TestContext context, BaseT base, TestResultSink sink) throws IOException {
		if(sink != null)
			sink.beginTestcase(name);
		RefCell<Throwable> except = new RefCell<Throwable>();
		CapturedOutput capturedOutput;
		try {
			capturedOutput = TestUtils.captureOutput(() -> performTest(context, base), except::setValue);
		}
		catch(Throwable t) {
			if(t instanceof Error)
				throw (Error)t;
			if(t instanceof RuntimeException)
				throw (RuntimeException)t;
			throw new Error("Exception should not have been possible to throw", t);
		}
		if(sink == null)
			return;
		TestcaseResult result;
		Throwable thrown = except.getValue();
		if(thrown == null) {
			if(expectedException != null)
				result = new SimpleTestcaseResult(name, new ExpectedExceptionNotThrownError(expectedException,
						expectedExceptionName), capturedOutput);
			else
				result = new SimpleTestcaseResult(name, capturedOutput);
		}
		else if(thrown instanceof AssertionFailureError)
			result = new SimpleTestcaseResult(name, (AssertionFailureError)thrown, capturedOutput);
		else if(thrown instanceof AssumptionFailureError)
			result = new SimpleTestcaseResult(name, (AssumptionFailureError)thrown, capturedOutput);
		else if(expectedException == null)
			result = new SimpleTestcaseResult(name, new UnexpectedExceptionThrownError(thrown), capturedOutput);
		else if(expectedException.test(thrown))
			result = new SimpleTestcaseResult(name, capturedOutput);
		else
			result = new SimpleTestcaseResult(name, new WrongExceptionThrownError(expectedException,
					expectedExceptionName, thrown), capturedOutput);
		sink.endTestcase(name, result);
	}

}
