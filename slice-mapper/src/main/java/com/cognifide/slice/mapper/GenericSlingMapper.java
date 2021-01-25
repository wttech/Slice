/*-
 * #%L
 * Slice - Mapper
 * %%
 * Copyright (C) 2012 Wunderman Thompson Technology
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

package com.cognifide.slice.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.mapper.annotation.IgnoreProperty;
import com.cognifide.slice.mapper.annotation.ImagePath;
import com.cognifide.slice.mapper.annotation.JcrProperty;
import com.cognifide.slice.mapper.annotation.MappingStrategy;
import com.cognifide.slice.mapper.annotation.SliceResource;
import com.cognifide.slice.mapper.annotation.Unescaped;
import com.cognifide.slice.mapper.api.Mapper;
import com.cognifide.slice.mapper.api.processor.FieldPostProcessor;
import com.cognifide.slice.mapper.api.processor.FieldProcessor;
import com.cognifide.slice.mapper.exception.MapperException;
import com.cognifide.slice.mapper.helper.ReflectionHelper;
import com.cognifide.slice.mapper.impl.processor.DefaultFieldProcessor;
import com.cognifide.slice.mapper.strategy.MapperStrategy;
import com.cognifide.slice.mapper.strategy.MapperStrategyFactory;

/**
 * Generic implementation of {@link Mapper} that maps Sling {@link Resource} to a {@link SliceResource} using
 * reflection.<br>
 * 
 * It is assumed that SliceResource field names are exactly the same as property names in the JCR node
 * represented by the resource. Mapper iterates over fields in the SliceResource and assigns values from
 * resource to SliceResource using mapping strategy defined by the SliceResource.<br>
 * <br>
 * 
 * The mapper is extendable - it can process fields depending on a list of specified {@link FieldProcessor}s.
 * The mapper uses at least one {@link FieldProcessor} which is {@link DefaultFieldProcessor}.<br>
 * <br>
 * 
 * A field in SliceResource must be assignable from type of associated field in the repository, or an
 * exception will be thrown during mapping. Using primitive types supported, but discouraged: if corresponding
 * property is missing, the SliceResource should contain <tt>null</tt> value, and a higher level logic should
 * handle it and assign default value. Such behavior is not possible with primitive types.<br>
 * <br>
 * 
 * Once a value of a field has been set (mapped) it can be post-processed by specified
 * {@link FieldPostProcessor}s.<br>
 * <br>
 * 
 * SliceResource fields can also be tagged with annotations to enable custom logic. Currently supported
 * annotations are:
 * <ul>
 * <li>
 * FieldProcessors:
 * <ul>
 * <li>{@link JcrProperty} - for mapping field to a property of different name, or defining that a field
 * should be mapped if {@link MappingStrategy#ANNOTATED} was chosen</li>
 * <li>{@link IgnoreProperty} - those will be ignored by this Mapper if {@link MappingStrategy#ALL} was chosen
 * </li>
 * <li>{@link ImagePath} - indicating Image resource type which should be mapped to String field</li>
 * </ul>
 * </li>
 * <li>
 * FieldPostProcessors:
 * <ul>
 * <li>{@link Unescaped} - for string values that should not be HTML-escaped</li>
 * </ul>
 * </li>
 * </ul>
 * See each annotation documentation for more detailed use guidelines.
 * 
 */
public class GenericSlingMapper implements Mapper {

	/** common logger */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	private final MapperStrategyFactory mapperStrategyFactory = new MapperStrategyFactory();

	private final List<FieldProcessor> processors = new ArrayList<FieldProcessor>();

	private final List<FieldPostProcessor> postProcessors = new ArrayList<FieldPostProcessor>();

	GenericSlingMapper(MapperBuilder builder) {
		processors.addAll(builder.getProcessors());
		postProcessors.addAll(builder.getPostProcessors());
	}

	// /////////////////////////////////////////////////////////////////////////
	// com.cognifide.slice.mapper.api.Mapper implementation
	// ///////////////////////////////////////////////////////////////////////

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T get(final Resource resource, final T object) {
		if (resource == null) {
			throw new IllegalArgumentException("Resource cannot be null!");
		}
		return mapResourceToObject(resource, object);
	}

	// /////////////////////////////////////////////////////////////////////////
	// methods
	// ///////////////////////////////////////////////////////////////////////

	/**
	 * Maps resource to a SliceResource object using reflection. See {@link GenericSlingMapper} documentation
	 * for detailed behavior description.
	 * 
	 * @param resource resource to be mapped; cannot be null or empty
	 * @return SliceResource instance with fields populated from resource (if associated properties are
	 * present in the resource).
	 * @throws IllegalArgumentException if given resource was null or empty
	 */
	private <T> T mapResourceToObject(final Resource resource, final T object) {
		ValueMap valueMap = resource.adaptTo(ValueMap.class);
		try {
			Class<?> type = object.getClass();
			Field[] fields = ReflectionHelper.readAllDeclaredFields(type);
			for (Field field : fields) {
				mapResourceToField(resource, object, valueMap, field);
			}
			return object;
		} catch (Exception e) {
			String path = resource.getPath();
			String format = "[path={0}]: cannot map to object({1})";
			String message = MessageFormat.format(format, path, e.getMessage());
			logger.warn(message);
			throw new MapperException("mapResourceToObject failed", e);
		}
	}

	private <T> void mapResourceToField(final Resource resource, final T object, ValueMap valueMap,
			Field field) throws IllegalAccessException {
		MapperStrategy mapperStrategy = mapperStrategyFactory.getMapperStrategy(field.getDeclaringClass());
		if (shouldFieldBeMapped(field, mapperStrategy)) {
			Object value = getValueForField(resource, valueMap, field);
			if (value == null && field.getType().isPrimitive()) {
				// don't write null values to primitive
				return;
			}
			FieldUtils.writeField(field, object, value, ReflectionHelper.FORCE_ACCESS);
		}
	}

	private boolean shouldFieldBeMapped(Field field, MapperStrategy mapperStrategy) {
		return isFieldAssignable(field) && mapperStrategy.shouldFieldBeMapped(field);
	}

	/**
	 * Returns true if a field can be assigned, i.e. is not final, nor static
	 * 
	 * @param field the field being investigated
	 * @return true if the field is assignable, false otherwise
	 */
	private boolean isFieldAssignable(Field field) {
		int modifiers = field.getModifiers();
		if (Modifier.isFinal(modifiers)) {
			return false;
		} // else

		if (Modifier.isStatic(modifiers)) {
			return false;
		} // else
		return true;
	}

	/**
	 * Maps given resource to an object that can be assigned to a given field.
	 * 
	 * @param resource resource to be mapped; cannot be null or empty
	 * @param valueMap value map from a resource; passed for efficiency reasons only
	 * @param field used to deduce assignable value type and get meta-data from field's annotations
	 * @return an object that can be assigned to a given field
	 * @throws RuntimeException if given resource was null or empty
	 */
	private Object getValueForField(Resource resource, ValueMap valueMap, Field field) {
		Object value = null;
		String propertyName = getPropertyName(field);
		for (FieldProcessor fieldProcessor : processors) {
			if (fieldProcessor.accepts(resource, field)) {
				value = fieldProcessor.mapResourceToField(resource, valueMap, field, propertyName);
				break;
			}
		}
		for (FieldPostProcessor fieldProcessor : postProcessors) {
			if (fieldProcessor.accepts(resource, field, value)) {
				value = fieldProcessor.processValue(resource, field, value);
			}
		}
		return value;
	}

	/**
	 * Gets name of property associated with given field. Usually - same as field name, but can be overridden
	 * using {@link JcrProperty} annotation.
	 * 
	 * @param field field to get associated property name for
	 * @return property name associated with given field, never null.
	 */
	private String getPropertyName(Field field) {
		final JcrProperty annotation = field.getAnnotation(JcrProperty.class);
		if ((annotation != null) && StringUtils.isNotBlank(annotation.value())) {
			return annotation.value();
		}
		return field.getName();
	}

}
