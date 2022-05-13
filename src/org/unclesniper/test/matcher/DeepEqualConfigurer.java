package org.unclesniper.test.matcher;

import org.unclesniper.test.deepeq.DeepCompareConfig;

public interface DeepEqualConfigurer<SubjectT> extends Matcher<SubjectT, SubjectT> {

	DeepEqualConfigurer<SubjectT> using(DeepCompareConfig config);

}
