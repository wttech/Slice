package com.cognifide.slice.mapper.annotation;

/**
 * @author Mariusz Kubiś Date: 10.04.15
 */
@SliceResource
public class FollowModel {

	@Follow()
	@JcrProperty
	private JcrPropertyModel jcrPropertyModel;

	@JcrProperty(value = "jcrPropertyModel")
	private String jcrPropertyModelPath;

	public JcrPropertyModel getJcrPropertyModel() {
		return jcrPropertyModel;
	}

	public String getJcrPropertyModelPath() {
		return jcrPropertyModelPath;
	}
}
