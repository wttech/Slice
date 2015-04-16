package com.cognifide.slice.mapper.annotation;

/**
 * @author Mariusz Kubiś Date: 15.04.15
 */
@SliceResource
public class InheritanceModel extends AbstractInheritanceModel {

	@JcrProperty
	private String field2;

	public String getField2() {
		return field2;
	}
}
