package org.unclesniper.test;

public interface FlowTestsuite<OuterBaseT, InnerBaseT, SelfT extends FlowTestsuite<OuterBaseT, InnerBaseT, SelfT>>
		extends Testable<OuterBaseT> {

	SelfT test(Testable<? super InnerBaseT> test);

}
