package com.cognifide.slice.mapper.annotation;

/**
 * @author Mariusz Kubiś Date: 14.04.15
 */
@SliceResource
public class SliceReferenceModel {

	@JcrProperty
	private String text;

	@SliceReference("/content/foo/jcr:content")
	@JcrProperty
	private JcrPropertyModel propertyModel;

	public String getText() {
		return text;
	}

	public JcrPropertyModel getPropertyModel() {
		return propertyModel;
	}
}
