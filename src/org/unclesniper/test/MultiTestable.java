package org.unclesniper.test;

import java.util.List;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Collections;
import java.util.stream.Stream;

public class MultiTestable<BaseT> implements Testable<BaseT> {

	private final List<Testable<? super BaseT>> tests = new LinkedList<Testable<? super BaseT>>();

	public MultiTestable() {}

	public MultiTestable(Iterable<? extends Testable<? super BaseT>> tests) {
		if(tests != null) {
			for(Testable<? super BaseT> test : tests) {
				if(test != null)
					this.tests.add(test);
			}
		}
	}

	public MultiTestable(Stream<? extends Testable<? super BaseT>> tests) {
		if(tests != null) {
			tests.forEach(test -> {
				if(test != null)
					this.tests.add(test);
			});
		}
	}

	public List<Testable<? super BaseT>> getTests() {
		return Collections.unmodifiableList(tests);
	}

	public void addTest(Testable<? super BaseT> test) {
		if(test != null)
			tests.add(test);
	}

	@Override
	public void performTest(TestContext context, BaseT base, TestResultSink sink) throws IOException {
		for(Testable<? super BaseT> test : tests)
			test.performTest(context, base, sink);
	}

}
