package com.cognifide.slice.api.qualifier;

import com.cognifide.slice.mapper.annotation.SliceResource;
import com.google.inject.Inject;

/**
 * Created by Jaromir Celejewski on 2015-04-22.
 */
@SliceResource
public class SuffixInjectionModel {

	private String suffix;

	@Inject
	public SuffixInjectionModel(@Suffix String suffix) {
		this.suffix = suffix;
	}

	public String getSuffix() {
		return suffix;
	}
}
