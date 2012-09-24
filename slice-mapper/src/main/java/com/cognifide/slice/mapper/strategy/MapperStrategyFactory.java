package com.cognifide.slice.mapper.strategy;

/*
 * #%L
 * Slice - Mapper
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2012 Cognifide Limited
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import com.cognifide.slice.mapper.annotation.MappingStrategy;
import com.cognifide.slice.mapper.annotation.SliceResource;
import com.cognifide.slice.mapper.strategy.impl.AllFieldMapperStrategy;
import com.cognifide.slice.mapper.strategy.impl.AnnotatedFieldMapperStrategy;

public class MapperStrategyFactory {

	private final AllFieldMapperStrategy allFieldMapperStrategy = new AllFieldMapperStrategy();

	private final AnnotatedFieldMapperStrategy annotatedFieldMapperStrategy = new AnnotatedFieldMapperStrategy();

	/**
	 * Returns appropriate {@link MapperStrategy} for specified class. The decision which strategy to choose
	 * is made basing on {@link SliceResource} parameter
	 * 
	 * @param type
	 * @return
	 */
	public MapperStrategy getMapperStrategy(Class<?> type) {
		MapperStrategy defaultStrategy = annotatedFieldMapperStrategy;
		SliceResource sliceResource = type.getAnnotation(SliceResource.class);
		if (sliceResource != null) {
			final MappingStrategy mappingStrategy = sliceResource.value();
			return getStrategy(mappingStrategy, defaultStrategy);
		}
		return defaultStrategy;
	}

	private MapperStrategy getStrategy(MappingStrategy mappingStrategy, MapperStrategy defaultStrategy) {
		switch (mappingStrategy) {
			case ALL:
				return allFieldMapperStrategy;
			case ANNOTATED:
				return annotatedFieldMapperStrategy;
			default:
				return defaultStrategy;
		}
	}

}
