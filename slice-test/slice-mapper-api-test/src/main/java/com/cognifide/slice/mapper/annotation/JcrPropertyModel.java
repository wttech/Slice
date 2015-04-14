package com.cognifide.slice.mapper.annotation;

/**
 * @author Mariusz Kubiś Date: 10.04.15
 */

@SliceResource
public class JcrPropertyModel {

	@JcrProperty
	private String text;

	@JcrProperty(value = "style")
	private String secondProperty;

	@JcrProperty
	private int size;

	public String getText() {
		return text;
	}

	public String getSecondProperty() {
		return secondProperty;
	}

	public int getSize() {
		return size;
	}
}
