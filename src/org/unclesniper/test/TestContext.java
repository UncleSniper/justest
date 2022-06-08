package org.unclesniper.test;

import static org.unclesniper.test.TestUtils.notNull;

public interface TestContext {

	Object getContextObject(String key);

	String getContextParameter(String name);

	default <T> T getContextObject(String key, Class<T> type) {
		notNull(type, "Desired type");
		Object value = this.getContextObject(key);
		if(value == null)
			return null;
		if(!type.isInstance(value))
			throw new IllegalStateException("Expected context object '" + key + "' to be of type " + type.getName()
					+ ", but was of type " + value.getClass().getName());
		return type.cast(value);
	}

	default Object requireContextObject(String key) {
		Object value = this.getContextObject(key);
		if(value == null)
			throw new IllegalStateException("Expected context object '" + key + "' to be present, but is not");
		return value;
	}

	default <T> T requireContextObject(String key, Class<T> type) {
		T value = this.getContextObject(key, type);
		if(value == null)
			throw new IllegalStateException("Expected context object '" + key + "' to be present, but is not");
		return value;
	}

}
