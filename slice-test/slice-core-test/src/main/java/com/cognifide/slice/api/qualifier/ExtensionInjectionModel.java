package com.cognifide.slice.api.qualifier;

import com.cognifide.slice.mapper.annotation.SliceResource;
import com.google.inject.Inject;

/**
 * Created by Jaromir Celejewski on 2015-04-22.
 */
@SliceResource
public class ExtensionInjectionModel {

	private String extension;

	@Inject
	public ExtensionInjectionModel(@Extension String extension) {
		this.extension = extension;
	}

	public String getExtension() {
		return extension;
	}
}
