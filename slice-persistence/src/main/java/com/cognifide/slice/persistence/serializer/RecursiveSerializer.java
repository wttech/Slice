/*-
 * #%L
 * Slice - Persistence
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
package com.cognifide.slice.persistence.serializer;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;

import com.cognifide.slice.mapper.annotation.JcrProperty;
import com.cognifide.slice.mapper.annotation.SliceResource;
import com.cognifide.slice.persistence.api.ObjectSerializer;
import com.cognifide.slice.persistence.api.SerializerContext;

public class RecursiveSerializer implements ObjectSerializer {

	@Override
	public boolean accepts(Class<?> clazz) {
		return clazz.isAnnotationPresent(SliceResource.class);
	}

	@Override
	public void serialize(String childName, Object object, Resource parent, SerializerContext ctx)
			throws PersistenceException {
		if (ctx.alreadySerialized(object)) {
			return;
		}

		Resource child = parent.getChild(childName);
		if (child == null) {
			child = parent.getResourceResolver().create(parent, childName,
					SerializerContext.getInitialProperties());
		}
		for (final Field field : object.getClass().getDeclaredFields()) {
			if (!field.isAnnotationPresent(JcrProperty.class)) {
				continue;
			}

			final JcrProperty jcrPropAnnotation = field.getAnnotation(JcrProperty.class);
			final String overridingPropertyName = jcrPropAnnotation.value();
			final String fieldName = field.getName();
			final String propertyName = StringUtils.defaultIfEmpty(overridingPropertyName, fieldName);
			field.setAccessible(true);
			Object fieldValue;
			try {
				fieldValue = field.get(object);
			} catch (IllegalAccessException e) {
				throw new PersistenceException("Can't get field", e);
			}
			if (fieldValue != null) {
				ctx.getFacade().serialize(field, propertyName, fieldValue, child, ctx);
			}
		}
	}

	@Override
	public int getRank() {
		return Integer.MAX_VALUE;
	}

}
