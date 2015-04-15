package com.cognifide.slice.mapper.annotation;

/**
 * @author Mariusz Kubiś Date: 15.04.15
 */
@SliceResource
public class UnescapedModel {

	@JcrProperty
	@Unescaped
	private String field1;

	@JcrProperty
	private String field2;

	public String getField1() {
		return field1;
	}

	public String getField2() {
		return field2;
	}
}
