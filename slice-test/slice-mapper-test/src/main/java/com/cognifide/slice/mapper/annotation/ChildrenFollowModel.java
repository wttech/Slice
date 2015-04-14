package com.cognifide.slice.mapper.annotation;

import java.util.List;

/**
 * @author Mariusz Kubiś Date: 14.04.15
 */
@SliceResource
public class ChildrenFollowModel {

	@JcrProperty
	private String text;

	@Children(JcrPropertyModel.class)
	@JcrProperty(value = "children")
	@Follow
	private List<JcrPropertyModel> childrenList;

	public String getText() {
		return text;
	}

	public List<JcrPropertyModel> getChildrenList() {
		return childrenList;
	}
}
