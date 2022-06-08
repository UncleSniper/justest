package org.unclesniper.test.matcher;

import org.unclesniper.test.IndentSink;

public class IdentityMatcher<SubjectT> implements Matcher<SubjectT, SubjectT> {

	public IdentityMatcher() {}

	@Override
	public SubjectT match(SubjectT subject, boolean assume) {
		return subject;
	}

	@Override
	public boolean matches(SubjectT actual) {
		return true;
	}

	@Override
	public void describe(IndentSink sink) {}

}
