package com.cognifide.slice.mapper.strategy;

/*-
 * #%L
 * Slice - Mapper
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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cognifide.slice.mapper.annotation.MappingStrategy;
import com.cognifide.slice.mapper.annotation.SliceResource;
import com.cognifide.slice.mapper.strategy.impl.AllFieldMapperStrategy;
import com.cognifide.slice.mapper.strategy.impl.AnnotatedFieldMapperStrategy;

public class MapperStrategyFactory {

	private final AllFieldMapperStrategy allFieldMapperStrategy = new AllFieldMapperStrategy();

	private final AnnotatedFieldMapperStrategy annotatedFieldMapperStrategy = new AnnotatedFieldMapperStrategy();

	/**
	 * Caching map for mapper strategy. Used to reduce usage of expensive annotation parsing.
	 */
	private static final Map<Class<?>, MapperStrategy> MAPPER_STRATEGY_CACHE = new ConcurrentHashMap<Class<?>, MapperStrategy>(
			256);

	/**
	 * Returns appropriate {@link MapperStrategy} for specified class. The decision which strategy to choose
	 * is made basing on {@link SliceResource} parameter
	 * 
	 * @param type
	 * @return mapperStrategy
	 */
	public MapperStrategy getMapperStrategy(Class<?> type) {
		MapperStrategy defaultStrategy = annotatedFieldMapperStrategy;

		MapperStrategy cachedMapperStrategy = getStrategyFromCache(type);
		if (cachedMapperStrategy != null) {
			return cachedMapperStrategy;
		}

		SliceResource sliceResource = type.getAnnotation(SliceResource.class);
		if (sliceResource != null) {
			final MappingStrategy mappingStrategy = sliceResource.value();
			MapperStrategy mapperStrategy = getStrategy(mappingStrategy, defaultStrategy);
			putStrategyIntoCache(type, mapperStrategy);
			return mapperStrategy;
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

	private MapperStrategy getStrategyFromCache(Class<?> clazz) {
		return MAPPER_STRATEGY_CACHE.get(clazz);
	}

	private void putStrategyIntoCache(Class<?> clazz, MapperStrategy mapperStrategy) {
		MAPPER_STRATEGY_CACHE.put(clazz, mapperStrategy);
	}

}
