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

@SliceResource
class ChildrenModelWithInvalidReference {

	@Children(JcrPropertyModel.class)
	@JcrProperty(value = "/children")
	private List<JcrPropertyModel> childrenList;
}

@SliceResource
class ChildrenModelWithInvalidChildrenClass {

	@Children(BooleanInjectionModel.class)
	@JcrProperty(value = "children")
	private JcrPropertyModel[] childrenList;
}

@SliceResource
class ChildResourceNodeModel {

	@JcrProperty
	private JcrPropertyModel jcrPropertyModel;

	@JcrProperty
	private String text;

	public JcrPropertyModel getJcrPropertyModel() {
		return jcrPropertyModel;
	}

	public String getText() {
		return text;
	}
}
