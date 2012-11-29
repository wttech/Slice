package com.cognifide.slice.mapper.impl;

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


import java.lang.reflect.Field;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import com.cognifide.slice.api.provider.ModelProvider;
import com.cognifide.slice.mapper.SliceReferencePathResolver;
import com.cognifide.slice.mapper.annotation.SliceReference;
import com.cognifide.slice.mapper.api.processor.FieldProcessor;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class SliceReferenceFieldProcessor implements FieldProcessor {

	@Inject
	private Provider<ModelProvider> modelProvider;

	@Inject
	private Injector injector;

	@Override
	public boolean accepts(final Resource resource, final Field field) {
		return field.isAnnotationPresent(SliceReference.class);
	}

	@Override
	public Object mapResourceToField(final Resource resource, final ValueMap valueMap, final Field field,
			final String propertyName) {
		final SliceReference sliceReferenceAnnotation = field.getAnnotation(SliceReference.class);
		final String initialPath = sliceReferenceAnnotation.value();
		final String fullPath = getFullPath(initialPath);
		final Class<?> fieldType = field.getType();
		return modelProvider.get().get(fieldType, fullPath);
	}

	private String getFullPath(final String initialPath) {
		if (StringUtils.isBlank(initialPath)) {
			return StringUtils.EMPTY;
		}

		final String fullPath = getResolvedPath(initialPath);
		if (StringUtils.isBlank(fullPath)) {
			return StringUtils.EMPTY;
		}

		if ((fullPath.charAt(0) != '/') && !StringUtils.startsWith(fullPath, "./")) {
			return "./" + fullPath;
		}
		return fullPath;
	}

	private String getResolvedPath(final String initialPath) {
		if (StringUtils.isBlank(initialPath)) {
			return StringUtils.EMPTY;
		}

		// lazy loading of SliceReferencePathResolver - preventing errors in case one doesn't use
		// SliceReference at all
		final SliceReferencePathResolver sliceReferencePathResolver = injector
				.getInstance(SliceReferencePathResolver.class);
		return sliceReferencePathResolver.resolvePlaceholdersInPath(initialPath);
	}

}
