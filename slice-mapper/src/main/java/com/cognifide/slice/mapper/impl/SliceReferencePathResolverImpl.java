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

package com.cognifide.slice.mapper.impl;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.cognifide.slice.api.scope.ContextScoped;
import com.cognifide.slice.mapper.api.SliceReferencePathResolver;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * Default implementation of {@link SliceReferencePathResolver}
 * 
 * @author maciej.majchrzak
 * 
 */
@ContextScoped
public class SliceReferencePathResolverImpl implements SliceReferencePathResolver {

	private static final String PLACEHOLDER_REGEXP = "\\$\\{([\\w\\d-_]+)\\}";

	private static final Pattern PATTERN = Pattern.compile(PLACEHOLDER_REGEXP);

	private final Map<String, Class<? extends Annotation>> references = new HashMap<String, Class<? extends Annotation>>();

	private final Map<String, String> directReferences = new HashMap<String, String>();

	private final Injector injector;

	@Inject
	public SliceReferencePathResolverImpl(Injector injector) {
		this.injector = injector;
	}

	@Override
	public void addPlaceholder(String placeholder, Class<? extends Annotation> annotationClass) {
		references.put(placeholder, annotationClass);
	}

	@Override
	public void addPlaceholder(String placeholder, String directReference) {
		directReferences.put(placeholder, directReference);
	}

	@Override
	public String resolvePlaceholdersInPath(final String initialPath) {
		String path = initialPath;
		Matcher matcher = PATTERN.matcher(initialPath);
		while (matcher.find()) {
			String placeholder = matcher.group(1);
			String replacement;
			Class<? extends Annotation> annotationClass = getAnnotation(placeholder);
			if (annotationClass != null) {
				replacement = injector.getInstance(Key.get(String.class, annotationClass));
			} else {
				replacement = getDirectReference(placeholder);
			}
			String group = matcher.group();
			path = StringUtils.replaceOnce(path, group, replacement);
		}
		return path;
	}

	private Class<? extends Annotation> getAnnotation(String placeholder) {
		return references.get(placeholder);
	}

	private String getDirectReference(String placeholder) {
		return directReferences.get(placeholder);
	}

}
