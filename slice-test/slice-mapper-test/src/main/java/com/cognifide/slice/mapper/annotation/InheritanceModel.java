package com.cognifide.slice.mapper.annotation;

/**
 * @author Mariusz Kubiś Date: 15.04.15
 */
@SliceResource(MappingStrategy.ANNOTATED)
class InheritanceModelAnnotated extends AbstractInheritanceModelAnnotated {

	@JcrProperty
	private String field2;

	public String getField2() {
		return field2;
	}
}

@SliceResource(MappingStrategy.ANNOTATED)
class InheritanceModelAnnotatedAll extends AbstractInheritanceModelAll {

	@JcrProperty
	private String field2;

	public String getField2() {
		return field2;
	}
}

@SliceResource(MappingStrategy.ANNOTATED)
class InheritanceModelAnnotatedWithout extends AbstractInheritanceModelWithout {

	@JcrProperty
	private String field2;

	public String getField2() {
		return field2;
	}
}

@SliceResource(MappingStrategy.ALL)
class InheritanceModelAll extends AbstractInheritanceModelAll {

	private String field2;

	public String getField2() {
		return field2;
	}
}

@SliceResource(MappingStrategy.ALL)
class InheritanceModelAllAnnotated extends AbstractInheritanceModelAnnotated {

	private String field2;

	public String getField2() {
		return field2;
	}
}

@SliceResource(MappingStrategy.ALL)
class InheritanceModelAllWithout extends AbstractInheritanceModelWithout {

	private String field2;

	public String getField2() {
		return field2;
	}
}

class InheritanceModelWithoutAll extends AbstractInheritanceModelAll {

	private String field2;

	public String getField2() {
		return field2;
	}
}

class InheritanceModelWithoutAnnotated extends AbstractInheritanceModelAnnotated {

	private String field2;

	public String getField2() {
		return field2;
	}
}
