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

package com.cognifide.slice.mapper.impl.processor;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import com.cognifide.slice.api.provider.ModelProvider;
import com.cognifide.slice.mapper.annotation.Children;
import com.cognifide.slice.mapper.annotation.Follow;
import com.cognifide.slice.mapper.api.processor.FieldProcessor;
import com.cognifide.slice.mapper.exception.MapperException;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ChildrenFieldProcessor implements FieldProcessor {

	private static final Set<Class> ASSIGNABLE_FIELD_TYPES;

	static {
		Set<Class> types = new HashSet<Class>();
		types.add(Set.class);
		types.add(List.class);
		types.add(Collection.class);
		ASSIGNABLE_FIELD_TYPES = Collections.unmodifiableSet(types);
	}

	private final Provider<ModelProvider> modelProvider;

	@Inject
	public ChildrenFieldProcessor(final Provider<ModelProvider> modelProvider) {
		this.modelProvider = modelProvider;
	}

	@Override
	public boolean accepts(Resource resource, Field field) {
		if (!field.isAnnotationPresent(Children.class)) {
			return false;
		}
		Class<?> fieldType = field.getType();
		return ASSIGNABLE_FIELD_TYPES.contains(fieldType) || fieldType.isArray();
	}

	@Override
	public Object mapResourceToField(Resource resource, ValueMap valueMap, Field field, String propertyName) {
		List<?> mappedModels;
		if (field.isAnnotationPresent(Follow.class)) {
			mappedModels = getRemoteChildrenList(resource, valueMap, field, propertyName);
		} else {
			mappedModels = getChildrenList(resource, field, propertyName);
		}

		final Class<?> fieldType = field.getType();
		if (fieldType.isArray()) {
			return getArrayFromList(fieldType.getComponentType(), mappedModels);
		}
		return Set.class.isAssignableFrom(fieldType) ? new LinkedHashSet(mappedModels) : mappedModels;
	}

	private List<?> getChildrenList(Resource resource, Field field, String propertyName) {
		if (propertyName.charAt(0) == '/') {
			throw new IllegalArgumentException(
					"Property name must not start with \"/\" as it doesn't indicate a relative resource");
		}
		Resource parentResource = resource.getChild(propertyName);
		return getChildrenList(parentResource, field);
	}

	private List<?> getChildrenList(Resource parentResource, Field field) {
		List<?> result;
		if (parentResource == null) {
			result = Collections.EMPTY_LIST;
		} else {
			Children childrenAnnotation = field.getAnnotation(Children.class);
			Class<?> modelClass = childrenAnnotation.value();
			result = modelProvider.get().getChildModels(modelClass, parentResource);
		}
		return result;
	}

	private Object getArrayFromList(Class<?> componentType, List<?> children) {
		Object array = Array.newInstance(componentType, children.size());
		int index = 0;
		for (Object child : children) {
			if (!componentType.isAssignableFrom(child.getClass())) {
				String message = MessageFormat.format("Can't cast {0} into {1} array", child, componentType);
				throw new MapperException(message);
			}
			Array.set(array, index++, child);
		}
		return array;
	}

	private List<?> getRemoteChildrenList(Resource resource, ValueMap valueMap, Field field,
			String propertyName) {
		Resource followUpResource = FollowUpProcessorUtil.getFollowUpResource(resource, valueMap, field,
				propertyName);

		if (followUpResource == null) {
			return Collections.EMPTY_LIST;
		}

		return getChildrenList(followUpResource, field);
	}

}
