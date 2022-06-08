package org.unclesniper.test.matcher;

import java.util.stream.Stream;
import org.unclesniper.test.Transform;
import org.unclesniper.test.TestUtils;
import org.unclesniper.test.IndentSink;
import org.unclesniper.test.Describeable;

import static org.unclesniper.test.TestUtils.notNull;

public class PropertyInfo extends AbstractInfo {

	private final Object base;

	private final Transform<?, ?> property;

	private final String propertyName;

	private final Matcher<?, ?> propertyMatcher;

	private final Object propertyValue;

	private final Throwable extractFailure;

	private final Describeable matchFailure;

	public PropertyInfo(Object base, Transform<?, ?> property, String propertyName, Matcher<?, ?> propertyMatcher,
			Object propertyValue, Throwable extractFailure, Describeable matchFailure) {
		if((extractFailure == null) == (matchFailure == null))
			throw new IllegalArgumentException("Exactly one of extractFailure or matchFailure must be non-null");
		this.base = base;
		this.property = notNull(property, "Property extractor function");
		this.propertyName = propertyName;
		this.propertyMatcher = notNull(propertyMatcher, "Property matcher");
		this.propertyValue = propertyValue;
		this.extractFailure = extractFailure;
		this.matchFailure = matchFailure;
	}

	public Object getBase() {
		return base;
	}

	public Transform<?, ?> getProperty() {
		return property;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Matcher<?, ?> getPropertyMatcher() {
		return propertyMatcher;
	}

	public Object getPropertyValue() {
		return propertyValue;
	}

	public Throwable getExtractFailure() {
		return extractFailure;
	}

	public Describeable getMatchFailure() {
		return matchFailure;
	}

	@Override
	protected void make(IndentSink sink) {
		if(propertyName == null)
			sink.append("Expected property of", false);
		else
			sink.append("Expected property '" + propertyName + "' of", false);
		sink.append(TestUtils.describeObject(base), true);
		propertyMatcher.describe(sink);
		if(extractFailure != null) {
			sink.append("but extracting the property value threw an exception:", false);
			sink.append(TestUtils.describeObject(extractFailure), true);
		}
		else {
			sink.append("but actual property value", false);
			sink.append(TestUtils.describeObject(propertyValue), true);
			sink.append("did not match:", false);
			Stream<String> lines = matchFailure.describe();
			if(lines != null) {
				lines.forEach(line -> {
					if(line != null)
						sink.append(line, true);
				});
			}
		}
	}

}
