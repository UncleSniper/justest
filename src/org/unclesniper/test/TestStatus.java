package org.unclesniper.test;

import org.unclesniper.test.resource.Localization;

public enum TestStatus {

	PASSED,
	SKIPPED,
	FAILED;

	private final String localizedName;

	private TestStatus() {
		localizedName = Localization.DEFAULT.getMessage(TestStatus.class.getName() + '.' + name().toLowerCase());
	}

	public String getLocalizedName() {
		return localizedName;
	}

}
