package com.cognifide.slice.mapper.annotation;

/**
 * @author Mariusz Kubiś Date: 15.04.15
 */
@SliceResource(MappingStrategy.ANNOTATED)
class AbstractInheritanceModelAnnotated {

	@JcrProperty
	private String field1;

	public String getField1() {
		return field1;
	}
}

@SliceResource(MappingStrategy.ALL)
class AbstractInheritanceModelAll {

	private String field1;

	public String getField1() {
		return field1;
	}
}

class AbstractInheritanceModelWithout {

	private String field1;

	public String getField1() {
		return field1;
	}
}
