package com.cognifide.slice.mapper.annotation;

/**
 * @author Mariusz Kubiś Date: 15.04.15
 */
@SliceResource(MappingStrategy.ALL)
public class MappingStrategyAllModel {

	private String field1;

	private String field2;

	private String field3;

	public MappingStrategyAllModel() {
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
