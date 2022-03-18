package org.unclesniper.test;

import java.util.List;
import java.io.IOException;
import java.util.stream.Stream;

public class SimpleTestsuite<BaseT> extends AbstractSimpleTestsuite<BaseT, BaseT>
		implements FlowTestsuite<BaseT, BaseT, SimpleTestsuite<BaseT>> {

	public SimpleTestsuite(String name) {
		super(name);
	}

	public SimpleTestsuite(String name, Iterable<? extends Testable<? super BaseT>> tests) {
		super(name, tests);
	}

	public SimpleTestsuite(String name, Stream<? extends Testable<? super BaseT>> tests) {
		super(name, tests);
	}

	@Override
	protected boolean performSuite(TestContext context, BaseT base, TestResultSink sink) throws IOException {
		List<Testable<? super BaseT>> tests = getTests();
		if(tests == null)
			return false;
		for(Testable<? super BaseT> test : tests) {
			if(test != null)
				test.performTest(context, base, sink);
		}
		return false;
	}

	@Override
	public SimpleTestsuite<BaseT> test(Testable<? super BaseT> test) {
		addTest(test);
		return this;
	}

}
