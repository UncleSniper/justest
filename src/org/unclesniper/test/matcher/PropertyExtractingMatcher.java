package org.unclesniper.test.matcher;

import org.unclesniper.test.Transform;
import org.unclesniper.test.IndentSink;
import org.unclesniper.test.Describeable;
import org.unclesniper.test.AssertionFailureError;
import org.unclesniper.test.AssumptionFailureError;

import static org.unclesniper.test.TestUtils.notNull;

public class PropertyExtractingMatcher<BaseT, PropertyT, OutT> implements Matcher<BaseT, OutT> {

	private final Transform<? super BaseT, ? extends PropertyT> property;

	private final String propertyName;

	private final Matcher<? super PropertyT, ? extends OutT> propertyMatcher;

	public PropertyExtractingMatcher(Transform<? super BaseT, ? extends PropertyT> property,
			String propertyName, Matcher<? super PropertyT, ? extends OutT> propertyMatcher) {
		this.property = notNull(property, "Property extractor function");
		this.propertyName = propertyName;
		this.propertyMatcher = notNull(propertyMatcher, "Property matcher");
	}

	public Transform<? super BaseT, ? extends PropertyT> getProperty() {
		return property;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Matcher<? super PropertyT, ? extends OutT> getPropertyMatcher() {
		return propertyMatcher;
	}

	@Override
	public OutT match(BaseT base, boolean assume) {
		PropertyT propertyValue = null;
		Throwable extractFailure = null;
		try {
			propertyValue = property.transform(base);
		}
		catch(Throwable t) {
			extractFailure = t;
		}
		Describeable matchFailure = null;
		if(extractFailure == null) {
			try {
				return propertyMatcher.match(propertyValue, assume);
			}
			catch(AssertionFailureError e) {
				if(assume)
					throw e;
				matchFailure = e;
			}
			catch(AssumptionFailureError e) {
				if(!assume)
					throw e;
				matchFailure = e;
			}
		}
		PropertyInfo info = new PropertyInfo(base, property, propertyName, propertyMatcher, propertyValue,
				extractFailure, matchFailure);
		if(assume)
			throw new PropertyAssumptionFailureError(info);
		else
			throw new PropertyAssertionFailureError(info);
	}

	@Override
	public boolean matches(BaseT base) {
		PropertyT propertyValue;
		try {
			propertyValue = property.transform(base);
		}
		catch(Throwable t) {
			return false;
		}
		return propertyMatcher.matches(propertyValue);
	}

	@Override
	public void describe(IndentSink sink) {
		notNull(sink, "Sink");
		if(propertyName == null)
			sink.append("to have a property and that property", false);
		else
			sink.append("to have a property '" + propertyName + "' and that property", false);
		propertyMatcher.describe(sink);
	}

}
