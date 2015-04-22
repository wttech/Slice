package com.cognifide.slice.mapper.annotation;

/**
 * @author Mariusz Kubiś Date: 15.04.15
 */
@SliceResource(MappingStrategy.ANNOTATED)
public class MappingStrategyAnnotatedModel {

	@JcrProperty
	private String field1;

	private String field2;

	@JcrProperty
	private String field3;

	public MappingStrategyAnnotatedModel() {
		this.field3 = "field3 init value";
	}

	public String getField1() {
		return field1;
	}

	public String getField2() {
		return field2;
	}

	public String getField3() {
		return field3;
	}
}
