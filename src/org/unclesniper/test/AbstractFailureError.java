package org.unclesniper.test;

import java.util.Collection;
import java.util.stream.Stream;
import java.util.function.Supplier;

public abstract class AbstractFailureError extends Error implements Describeable {

	private final Supplier<? extends Stream<String>> description;

	public AbstractFailureError(String message, Supplier<? extends Stream<String>> description) {
		this(message, description, null);
	}

	public AbstractFailureError(String message, Supplier<? extends Stream<String>> description, Throwable cause) {
		super(message, cause);
		this.description = description;
	}

	public AbstractFailureError(String message, Collection<String> description) {
		this(message, description, null);
	}

	public AbstractFailureError(String message, Collection<String> description, Throwable cause) {
		super(message, cause);
		this.description = () -> description == null ? null : description.stream();
	}

	@Override
	public Stream<String> describe() {
		Stream<String> stream = description == null ? null : description.get();
		return stream == null ? Stream.empty() : stream;
	}

}
