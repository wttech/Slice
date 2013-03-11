package com.cognifide.slice.mapper.impl.processor;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.api.provider.ModelProvider;
import com.cognifide.slice.mapper.annotation.Children;
import com.cognifide.slice.mapper.api.processor.FieldProcessor;
import com.cognifide.slice.mapper.exception.MapperException;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ChildrenFieldProcessor implements FieldProcessor {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Inject
	private Provider<ModelProvider> modelProvider;

	private Class<?> fieldType = null;
	
	@Override
	public boolean accepts(Resource resource, Field field) {
		logger.warn("ACCEPTATION");
		if (!field.isAnnotationPresent(Children.class)) {
			return false;
		}
		fieldType = field.getType();
		return Collection.class.isAssignableFrom(fieldType) || fieldType.isArray();
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public Object mapResourceToField(Resource resource, ValueMap valueMap, Field field, String propertyName) {
		logger.warn("MAPPING");
		if (fieldType == null) {
			String message = MessageFormat.format("The resource {0} is not an array or a list.", propertyName);
			throw new MapperException(message, new Exception());
		}
		
		final Children childrenAnnotation = field.getAnnotation(Children.class);
		final Class<?> childrenType = childrenAnnotation.type();
		final Class<?> fieldType = field.getType();
		Resource childResource = resource.getChild(propertyName);

		if (childResource != null) {
			Iterator<Resource> childrenIterator = childResource.listChildren();
			@SuppressWarnings("rawtypes")
			List children = new ArrayList();
			while (childrenIterator.hasNext()) {		
				children.add(modelProvider.get().get(childrenType, childrenIterator.next().getPath()));
			}
			logger.warn("LISTA"+field.getClass());
			return children;
		} else {
			String message = MessageFormat.format("The resource {0} does not exists.", propertyName);
			throw new MapperException(message, new Exception());
		}
	}

	private <T> T getProperCollectionType(@SuppressWarnings("rawtypes") final List children, final Class<T> fieldType) {
		if (Collection.class.isAssignableFrom(fieldType)) {
			return fieldType.cast(children);
		} else if (fieldType.isArray()) {
			logger.warn("TUTAJ" + fieldType.toString());
			return (fieldType.cast(children.toArray()));
		} else {
			String message = MessageFormat
					.format("The {0} is not an array or a list.", fieldType.toString());
			throw new MapperException(message, new Exception());
		}
	}
}
