package org.unclesniper.test;

import static org.unclesniper.test.TestUtils.notNull;

public class DelegatingInitializer<BaseT> implements Initializer<BaseT> {

	private final Initializer<? super BaseT> initializer;

	private final Boolean required;

	public DelegatingInitializer(Initializer<? super BaseT> initializer) {
		this(initializer, null);
	}

	public DelegatingInitializer(Initializer<? super BaseT> initializer, Boolean required) {
		this.initializer = notNull(initializer, "Initializer");
		this.required = required;
	}

	public Initializer<? super BaseT> getInitializer() {
		return initializer;
	}

	public Boolean getRequired() {
		return required;
	}

	@Override
	public void initializeBase(TestContext context, BaseT base, String baseName, boolean keepGoing,
			TestResultSink sink, boolean finalizing) throws Throwable {
		initializer.initializeBase(context, base, baseName, keepGoing, sink, finalizing);
	}

	@Override
	public boolean isRequired() {
		if(required != null)
			return required.booleanValue();
		return initializer.isRequired();
	}

}
