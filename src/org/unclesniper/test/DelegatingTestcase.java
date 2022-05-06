package org.unclesniper.test;

import java.util.function.Predicate;

import static org.unclesniper.test.TestUtils.notNull;

public class DelegatingTestcase<BaseT> extends AbstractTestcase<BaseT> {

	private final Testcase<? super BaseT> testcase;

	public DelegatingTestcase(String name, Testcase<? super BaseT> testcase) {
		this(name, testcase, null, null);
	}

	public DelegatingTestcase(String name, Testcase<? super BaseT> testcase,
			Predicate<? super Throwable> expectedException, String expectedExceptionName) {
		super(name, expectedException, expectedExceptionName);
		this.testcase = notNull(testcase, "Testcase");
	}

	@Override
	protected void performTest(TestContext context, BaseT base) throws Throwable {
		testcase.performTest(base, context == null ? EmptyTestContext.instance : context);
	}

}
