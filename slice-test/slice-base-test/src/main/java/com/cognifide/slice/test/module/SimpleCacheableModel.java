package com.cognifide.slice.test.module;

import com.cognifide.slice.api.scope.Cacheable;
import com.cognifide.slice.mapper.annotation.JcrProperty;
import com.cognifide.slice.mapper.annotation.SliceResource;

/**
 * Created by Jaromir Celejewski on 2015-04-21.
 */

@Cacheable
@SliceResource
public class SimpleCacheableModel {

	@JcrProperty
	private String prop1;

	public String getProp1() {
		return prop1;
	}
}