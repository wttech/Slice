package com.cognifide.slice.mapper.annotation;

/**
 * @author Krzysztof Watral
 */
@SliceResource
public class PostMappingModel extends BaseMappingModel {

	@PostMapping
	void postMapping() {
		modelSet = field != null;
		postfix = "_TEST";
	}
}
