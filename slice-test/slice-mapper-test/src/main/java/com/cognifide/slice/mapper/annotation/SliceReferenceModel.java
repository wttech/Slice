package com.cognifide.slice.mapper.annotation;

import org.apache.commons.lang.StringUtils;

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

@SliceResource
class SliceReferenceModelWithEmptyPlaceholderReference {

	@JcrProperty
	private String text;

	@SliceReference("${a0_}")
	@JcrProperty
	private JcrPropertyModel propertyModel;

	public String getText() {
		return text;
	}

	public JcrPropertyModel getPropertyModel() {
		return propertyModel;
	}
}

@SliceResource
class EmptySliceReferenceModel {
	@SliceReference(StringUtils.EMPTY)
	@JcrProperty
	private JcrPropertyModel propertyModel;

	JcrPropertyModel getPropertyModel() {
		return propertyModel;
	}
}

@SliceResource
class SliceReferenceRelativePathModel {
	@SliceReference("propertyModel")
	@JcrProperty
	private JcrPropertyModel propertyModel;

	JcrPropertyModel getPropertyModel() {
		return propertyModel;
	}
}