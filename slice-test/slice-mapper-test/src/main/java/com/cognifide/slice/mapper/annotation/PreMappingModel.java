package com.cognifide.slice.mapper.annotation;

/**
 * @author Krzysztof Watral
 */
@SliceResource
public class PreMappingModel extends BaseMappingModel {

	@PreMapping
	void preMapping() {
		modelSet = field != null;
		upperCase = true;
	}
}
