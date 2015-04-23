package com.cognifide.slice.mapper.annotation;

import java.util.List;

/**
 * @author Mariusz Kubiś Date: 14.04.15
 */
@SliceResource
public class ChildrenModel {

	@JcrProperty
	private String text;

	@Children(JcrPropertyModel.class)
	@JcrProperty(value = "children")
	private List<JcrPropertyModel> childrenList;

	@Children(JcrPropertyModel.class)
	@JcrProperty(value = "children")
	private JcrPropertyModel[] childrenArray;

	public String getText() {
		return text;
	}

	public List<JcrPropertyModel> getChildrenList() {
		return childrenList;
	}

    public JcrPropertyModel[] getChildrenArray() {
        return childrenArray;
    }
}
