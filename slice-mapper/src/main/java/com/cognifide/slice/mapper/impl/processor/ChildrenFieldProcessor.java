package com.cognifide.slice.mapper.impl.processor;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
		final Class<?> fieldType = field.getType();
		final String fieldName = field.getName();
		propertyName = (fieldName.equals(propertyName) ? "." : propertyName);

		@SuppressWarnings("rawtypes")
		List children = getChildrenList(resource, field, propertyName);

		if (fieldType.isArray()) {
			return getArrayFromList(fieldType, children);
		} else {
			return children;
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List getChildrenList(Resource resource, Field field, String propertyName) {
		final Resource childResource = resource.getChild(propertyName);
		final Children childrenAnnotation = field.getAnnotation(Children.class);
		final Class<?> childrenType = childrenAnnotation.value();

		if (childResource != null) {
			Iterator<Resource> childrenIterator = childResource.listChildren();
			List children = new ArrayList();
			while (childrenIterator.hasNext()) {
				String childrenPath = childrenIterator.next().getPath();
				Object child = modelProvider.get().get(childrenType, childrenPath);
				if (child != null) {
					children.add(child);
				}
			}
			return children;
		} else {
			String message = MessageFormat.format("The resource {0} does not exists.", propertyName);
			throw new MapperException(message, new Exception());
		}
	}

	@SuppressWarnings("rawtypes")
	private Object getArrayFromList(Class<?> fieldType, List children) {
		Class<?> ofArray = fieldType.getComponentType();
		try {
			return getCastedArray(children, ofArray.getConstructor().newInstance());
		} catch (Exception e) {
			String message = "Cannot cast from list to array";
			throw new MapperException(message, e);
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <T> T[] getCastedArray(List list, T type) {
		return (T[]) list.toArray((T[]) Array.newInstance(type.getClass(), list.size()));
	}
}
