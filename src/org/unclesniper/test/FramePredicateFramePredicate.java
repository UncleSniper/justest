package org.unclesniper.test;

import java.util.function.Predicate;

import static org.unclesniper.test.TestUtils.notNull;

public class FramePredicateFramePredicate implements Predicate<StackTraceElement> {

	private FramePredicate predicate;

	public FramePredicateFramePredicate(FramePredicate predicate) {
		this.predicate = predicate;
	}

	public FramePredicate getPredicate() {
		return predicate;
	}

	public void setPredicate(FramePredicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public boolean test(StackTraceElement frame) {
		notNull(frame, "Frame");
		if(predicate == null)
			throw new IllegalStateException("No child predicate set");
		String qname = frame.getClassName();
		int pos = qname.lastIndexOf('.');
		if(pos < 0)
			return predicate.test(frame, qname, null, qname, frame.getMethodName());
		else
			return predicate.test(frame, qname, qname.substring(0, pos), qname.substring(pos + 1),
					frame.getMethodName());
	}

}
