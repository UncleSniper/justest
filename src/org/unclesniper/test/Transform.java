package org.unclesniper.test;

import java.util.function.Function;

import static org.unclesniper.test.TestUtils.notNull;

@FunctionalInterface
public interface Transform<FromT, ToT> {

	ToT transform(FromT arg) throws Throwable;

	public static <FromT, ToT> Transform<FromT, ToT> of(Function<? super FromT, ? extends ToT> transform) {
		notNull(transform, "Transform");
		return arg -> transform.apply(arg);
	}

}
