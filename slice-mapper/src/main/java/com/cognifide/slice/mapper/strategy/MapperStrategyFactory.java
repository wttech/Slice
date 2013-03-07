package com.cognifide.slice.mapper.strategy;

/*-
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

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
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
	 * Map caching class mapping strategy. Used to reduce usage of reflection and expensive annotation
	 * parsing.
	 */
	private static Map<Class<?>, SoftReference<MappingStrategy>> mappingStrategyCache = new ConcurrentHashMap<Class<?>, SoftReference<MappingStrategy>>(
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

		MappingStrategy cacheMappingStrategy = getFieldsFromCache(type);
		if (cacheMappingStrategy != null) {
			return getStrategy(cacheMappingStrategy, defaultStrategy);
		}

		SliceResource sliceResource = type.getAnnotation(SliceResource.class);
		if (sliceResource != null) {
			final MappingStrategy mappingStrategy = sliceResource.value();
			putFieldsIntoCache(type, mappingStrategy);
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

	private static MappingStrategy getFieldsFromCache(Class<?> clazz) {
		SoftReference<MappingStrategy> softReferenceFields = mappingStrategyCache.get(clazz);
		MappingStrategy mappingStrategy = null;
		if (softReferenceFields != null) {
			mappingStrategy = softReferenceFields.get();
		}
		return mappingStrategy;
	}

	private static void putFieldsIntoCache(Class<?> clazz, MappingStrategy mappingStrategy) {
		mappingStrategyCache.put(clazz, new SoftReference<MappingStrategy>(mappingStrategy));
	}

}
