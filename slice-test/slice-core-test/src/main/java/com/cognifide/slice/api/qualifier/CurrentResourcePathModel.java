package com.cognifide.slice.api.qualifier;

import com.cognifide.slice.mapper.annotation.SliceResource;
import com.google.inject.Inject;

/**
 * @author Mariusz Kubiś Date: 10.04.15
 */
@SliceResource
public class CurrentResourcePathModel {

	private String currentResourcePath;

	@Inject
	public CurrentResourcePathModel(@CurrentResourcePath String currentResourcePath) {
		this.currentResourcePath = currentResourcePath;
	}

	public String getCurrentResourcePath() {
		return currentResourcePath;
	}
}
