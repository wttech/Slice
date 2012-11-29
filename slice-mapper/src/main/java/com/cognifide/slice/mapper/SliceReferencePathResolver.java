package com.cognifide.slice.mapper;

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


import java.lang.annotation.Annotation;

/**
 * Stores placeholders which are used to resolves paths used within {@link SliceReference}. It also resolve
 * such paths by replacing stored placeholders by actual values.
 * 
 * @author maciej.majchrzak
 * 
 */
public interface SliceReferencePathResolver {

	/**
	 * Adds a placeholder and bind it to a specified annotation. The specified annotation must be a
	 * {@link BindingAnnotation} and must be used for providing a String value by some module. Each
	 * placeholder stored using this method will be resolved by replacing it by a value returned by a provider
	 * of a String annotated with the specified annotation.
	 * 
	 * @param placeholder placeholder which will be replaced in final path. It should NOT contain
	 * placeholder's prefix and suffix: <b>${</b> and <b>}</b> - it should be a simple text. Allowed
	 * characters are: letters, numbers, dash, underscore. E.g. <code>"globalConfiguration"</code>
	 * @param annotationClass binding annotation which is used to provide a String value
	 */
	void addPlaceholder(String placeholder, Class<? extends Annotation> annotationClass);

	/**
	 * Adds a placeholder and bind it to a specified String value (directReference). Since the placeholder
	 * will be replaced by the value specified in directReference, the directReference should be an absolute
	 * path.
	 * 
	 * @param placeholder placeholder which will be replaced in final path. It should NOT contain
	 * placeholder's prefix and suffix: <b>${</b> and <b>}</b> - it should be a simple text. Allowed
	 * characters are: letters, numbers, dash, underscore. E.g. <code>"globalConfiguration"</code>
	 * @param directReference value which will exactly replace the placeholder. E.g.
	 * <code>"/content/test/en/configuration/global-configuration"</code>
	 */
	void addPlaceholder(String placeholder, String directReference);

	/**
	 * Resolve the specified path by replacing all placeholders. At first, there is an attempt to resolve
	 * placeholders using annotations. If a placeholder is not bound to any annotation, then there is an
	 * attempt to replace it by a String value specified by {@link #addReference(String, String)} method. If
	 * this attempt fails, the placeholder will not be resolved and the final path will contain it.
	 * 
	 * @param initialPath path with placeholders to be replaced, e.g.
	 * <code>"${globalConfigPage}/jcr:content/configComponent"</code>
	 * @return a path with replaced placeholders, e.g.
	 * <code>"/content/test/en/configuration/global-configuration/jcr:content/configComponent"</code>. Or a
	 * path containing placeholders which could not be resolved.
	 */
	String resolvePlaceholdersInPath(final String initialPath);
}
