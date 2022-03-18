package org.unclesniper.test;

public interface FlowInitializedTestsuite<
	OuterBaseT,
	InnerBaseT,
	SelfT extends FlowInitializedTestsuite<OuterBaseT, InnerBaseT, SelfT>
> extends FlowTestsuite<OuterBaseT, InnerBaseT, SelfT> {

	@SuppressWarnings("unchecked")
	SelfT initialize(Initializer<? super InnerBaseT>... initializers);

	@SuppressWarnings("unchecked")
	SelfT finalize(Initializer<? super InnerBaseT>... finalizers);

	@SuppressWarnings("unchecked")
	SelfT initializeEach(Initializer<? super InnerBaseT>... initializers);

	@SuppressWarnings("unchecked")
	SelfT finalizeEach(Initializer<? super InnerBaseT>... finalizers);

}
