package com.cognifide.slice.test.module;

import com.cognifide.slice.api.scope.Cacheable;

/**
 * Created by Jaromir Celejewski on 2015-04-21.
 */
@Cacheable
public class SimpleCacheablePojo {

	private String prop1;

	public String getProp1() {
		return prop1;
	}
}
