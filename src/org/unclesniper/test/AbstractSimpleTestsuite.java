package org.unclesniper.test;

import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import java.util.stream.Stream;

public abstract class AbstractSimpleTestsuite<OuterBaseT, InnerBaseT> extends AbstractTestsuite<OuterBaseT> {

	private final List<Testable<? super InnerBaseT>> tests = new LinkedList<Testable<? super InnerBaseT>>();

	public AbstractSimpleTestsuite(String name) {
		super(name);
	}

	public AbstractSimpleTestsuite(String name, Iterable<? extends Testable<? super InnerBaseT>> tests) {
		super(name);
		if(tests != null) {
			for(Testable<? super InnerBaseT> test : tests) {
				if(test != null)
					this.tests.add(test);
			}
		}
	}

	public AbstractSimpleTestsuite(String name, Stream<? extends Testable<? super InnerBaseT>> tests) {
		super(name);
		if(tests != null) {
			tests.forEach(test -> {
				if(test != null)
					this.tests.add(test);
			});
		}
	}

	public List<Testable<? super InnerBaseT>> getTests() {
		return Collections.unmodifiableList(tests);
	}

	public void addTest(Testable<? super InnerBaseT> test) {
		if(test != null)
			tests.add(test);
	}

}
