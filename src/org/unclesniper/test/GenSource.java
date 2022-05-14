package org.unclesniper.test;

import java.util.function.Supplier;

import static org.unclesniper.test.TestUtils.notNull;

@FunctionalInterface
public interface GenSource<OutT> {

	OutT draw() throws Throwable;

	public static <OutT> GenSource<OutT> of(Supplier<? extends OutT> source) {
		notNull(source, "Source");
		return () -> source.get();
	}

}
