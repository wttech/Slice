package com.cognifide.slice.test.module;

import com.cognifide.slice.mapper.annotation.JcrProperty;

/**
 * Created by Jaromir Celejewski on 2015-04-22.
 */
public class SimplePojo {

	@JcrProperty
	private String prop1;

	public String getProp1() {
		return prop1;
	}
}
