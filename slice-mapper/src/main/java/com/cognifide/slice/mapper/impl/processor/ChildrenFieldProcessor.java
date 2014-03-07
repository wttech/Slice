package com.cognifide.slice.mapper.impl.processor;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import com.cognifide.slice.api.provider.ModelProvider;
import com.cognifide.slice.mapper.annotation.Children;
import com.cognifide.slice.mapper.api.processor.FieldProcessor;
import com.cognifide.slice.mapper.exception.MapperException;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ChildrenFieldProcessor implements FieldProcessor {

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
		return List.class.isAssignableFrom(fieldType) || fieldType.isArray();
	}

	@Override
	public Object mapResourceToField(Resource resource, ValueMap valueMap, Field field, String propertyName) {
		List<?> mappedModels = getChildrenList(resource, field, propertyName);

		final Class<?> fieldType = field.getType();
		if (fieldType.isArray()) {
			return getArrayFromList(fieldType.getComponentType(), mappedModels);
		} else {
			return mappedModels;
		}
	}

	private List<?> getChildrenList(Resource resource, Field field, String propertyName) {
		List<?> result;
		Resource parentResource = resource.getChild(propertyName);
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
}
