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
package com.cognifide.slice.persistence.impl.serializer;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Collection;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;

import com.cognifide.slice.persistence.api.SerializerContext;
import com.cognifide.slice.persistence.api.FieldSerializer;
import com.cognifide.slice.persistence.api.Serializer;

@Component(immediate = true)
@Service(Serializer.class)
public class NativeCollectionSerializer implements FieldSerializer {

	@Override
	public int getPriority() {
		return 100;
	}

	@Override
	public boolean accepts(Field field) {
		final Type fieldType = field.getGenericType();
		if (fieldType instanceof ParameterizedType) {
			final Type collectionType = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
			return NativePropertySerializer.isNativeJcrClass((Class<?>) collectionType);
		} else {
			return false;
		}
	}

	@Override
	public void serialize(Field field, String propertyName, Object fieldValue, Resource parent,
			SerializerContext ctx) throws PersistenceException {

		if (fieldValue == null) {
			removeProperty(parent, propertyName);
		} else {
			serializeNativeCollection(parent, propertyName, fieldValue);
		}
	}

	private void removeProperty(Resource parent, String propertyName) {
		final ModifiableValueMap map = parent.adaptTo(ModifiableValueMap.class);
		map.remove(propertyName);
	}

	private void serializeNativeCollection(Resource parent, String propertyName, Object fieldValue) {
		final ModifiableValueMap map = parent.adaptTo(ModifiableValueMap.class);
		final Collection<?> collection = (Collection<?>) fieldValue;
		if (collection.isEmpty()) {
			map.put(propertyName, new String[0]);
			return;
		}
		final Object array = collectionToArray(collection);
		if (array == null) {
			return;
		}

		int i = 0;
		for (Object o : collection) {
			Array.set(array, i++, o);
		}
		map.put(propertyName, array);
	}

	private Object collectionToArray(final Collection<?> collection) {
		final int size = collection.size();
		final Object firstItem = collection.iterator().next();
		final Object array;
		if (firstItem instanceof Integer) {
			array = Array.newInstance(int.class, size);
		} else if (firstItem instanceof Long) {
			array = Array.newInstance(long.class, size);
		} else if (firstItem instanceof Double) {
			array = Array.newInstance(double.class, size);
		} else if (firstItem instanceof Boolean) {
			array = Array.newInstance(boolean.class, size);
		} else if (firstItem instanceof Calendar) {
			array = Array.newInstance(Calendar.class, size);
		} else if (firstItem instanceof InputStream) {
			array = Array.newInstance(InputStream.class, size);
		} else if (firstItem instanceof String) {
			array = Array.newInstance(String.class, size);
		} else {
			return null;
		}
		return array;
	}

}
