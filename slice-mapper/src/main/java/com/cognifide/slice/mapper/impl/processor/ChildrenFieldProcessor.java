package com.cognifide.slice.mapper.impl.processor;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Collection;
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

	@Inject
	private Provider<ModelProvider> modelProvider;

	@Override
	public boolean accepts(Resource resource, Field field) {
		if (!field.isAnnotationPresent(Children.class)) {
			return false;
		}
		Class<?> fieldType = field.getType();
		return Collection.class.isAssignableFrom(fieldType) || fieldType.isArray();
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
		final Resource collectionParentResource = resource.getChild(propertyName);
		if (collectionParentResource == null) {
			String message = MessageFormat.format("The resource {0} does not exists.", propertyName);
			throw new MapperException(message);
		}

		final Children childrenAnnotation = field.getAnnotation(Children.class);
		final Class<?> modelClass = childrenAnnotation.value();

		return modelProvider.get().getChildModels(modelClass, collectionParentResource);
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
