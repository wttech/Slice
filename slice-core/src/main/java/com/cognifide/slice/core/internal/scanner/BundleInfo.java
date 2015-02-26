package com.cognifide.slice.core.internal.scanner;

public class BundleInfo {

	private final String bundleNameFilter;

	private final String basePackage;

	public BundleInfo(String bundleNameFilter, String basePackage) {
		this.bundleNameFilter = bundleNameFilter;
		this.basePackage = basePackage;
	}

	public String getBundleNameFilter() {
		return bundleNameFilter;
	}

	public String getBasePackage() {
		return basePackage;
	}

}