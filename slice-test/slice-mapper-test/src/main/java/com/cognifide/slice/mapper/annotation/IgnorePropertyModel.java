package com.cognifide.slice.mapper.annotation;

/**
 * @author Mariusz Kubiś Date: 14.04.15
 */
@SliceResource(MappingStrategy.ALL)
class IgnorePropertyModelAll {

	private String text;

	@IgnoreProperty
	private String style;

	@IgnoreProperty
	private int size;

	public IgnorePropertyModelAll() {
		this.size = 10;
	}

	public String getText() {
		return text;
	}

	public String getStyle() {
		return style;
	}

	public int getSize() {
		return size;
	}
}

@SliceResource(MappingStrategy.ANNOTATED)
class IgnorePropertyModelAnnotated {

	@JcrProperty
	private String text;

	private String style;

	private int size;

	public IgnorePropertyModelAnnotated() {
		this.size = 10;
	}

	public String getText() {
		return text;
	}

	public String getStyle() {
		return style;
	}

	public int getSize() {
		return size;
	}
}
