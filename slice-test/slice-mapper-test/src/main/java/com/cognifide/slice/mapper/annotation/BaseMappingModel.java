package com.cognifide.slice.mapper.annotation;

import org.apache.commons.lang3.StringUtils;

/**
 * Krzysztof Watral
 */
public abstract class BaseMappingModel {

	@JcrProperty
	protected String field;

	protected boolean upperCase;

	protected String postfix;

	protected boolean modelSet;

	public boolean wasModelSet() {
		return modelSet;
	}

	public String getField() {
		return (upperCase ? field.toUpperCase() : field) + StringUtils.defaultString(postfix);
	}
}
