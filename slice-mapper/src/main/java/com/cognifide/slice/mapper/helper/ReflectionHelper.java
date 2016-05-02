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

package com.cognifide.slice.mapper.helper;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

/**
 * Contains methods mapping JCR or Sling objects to data transfer objects. Supports use of reflection
 * mechanisms.
 */
public final class ReflectionHelper {

	private ReflectionHelper() {
		// hidden constructor
	}

	/** force access value for writeField(Field, Object, Object, boolean) */
	public static final boolean FORCE_ACCESS = true;

	/**
	 * Map caching class fields. Used to reduce usage of reflection and expensive annotation parsing.
	 */
	private static Map<Class<?>, SoftReference<Field[]>> fieldsCache = new ConcurrentHashMap<Class<?>, SoftReference<Field[]>>(
			256);

	/**
	 * This method translates primitive types into their object representation. ValueMap expects non-primitive
	 * types hence the need of translation.
	 * 
	 * @param field type of this field will be translated
	 * @return object type of the field
	 */
	public static Class<?> getFieldType(Field field) {
		Class<?> type = field.getType();
		if (type.isPrimitive()) {
			if (type == boolean.class) {
				return Boolean.class;
			} else if (type == char.class) {
				return Character.class;
			} else if (type == byte.class) {
				return Byte.class;
			} else if (type == short.class) {
				return Short.class;
			} else if (type == int.class) {
				return Integer.class;
			} else if (type == double.class) {
				return Double.class;
			} else if (type == long.class) {
				return Long.class;
			} else if (type == float.class) {
				return Float.class;
			}
		}
		return type;
	}

	/**
	 * Returns an array of Field objects reflecting all the fields declared by the class or interface
	 * represented by this Class object. This includes public, protected, default (package) access, and
	 * private fields, as well as inherited fields (except for Object class). The elements in the array
	 * returned are not sorted and are not in any particular order. This method returns an array of length 0
	 * if the class or interface declares no fields, or if this Class object represents a primitive type, an
	 * array class, or void.
	 * 
	 * @param clazz class that should be queried for declared fields.=
	 * @return the array of Field objects representing all the declared fields of this class and super classes
	 * (except for Object class).
	 */
	public static Field[] readAllDeclaredFields(Class<?> clazz) {

		// check cache for clazz fields to avoid expensive annotation parsing
		Field[] fields = getFieldsFromCache(clazz);
		if (fields != null) {
			return fields;
		}

		// collect fields from all super classes
		List<Field[]> declaredFieldLists = new ArrayList<Field[]>();
		declaredFieldLists.add(clazz.getDeclaredFields());
		Class<?> superclass = clazz.getSuperclass();
		while (!superclass.equals(Object.class)) {
			declaredFieldLists.add(superclass.getDeclaredFields());
			superclass = superclass.getSuperclass();
		}

		if (declaredFieldLists.size() == 1) {
			// class has no superclass other than object, no need to merge arrays
			fields = declaredFieldLists.get(0);

		} else { // else - merge arrays
			int fieldsCount = 0;
			for (Field[] declaredFields : declaredFieldLists) {
				fieldsCount += declaredFields.length;
			}

			int destinationPosition = 0;
			Field[] mergedFields = new Field[fieldsCount];
			for (Field[] declaredFields : declaredFieldLists) {
				System.arraycopy(declaredFields, 0, mergedFields, destinationPosition, declaredFields.length);
				destinationPosition += declaredFields.length;
			}

			fields = mergedFields;
		}

		// put clazz fields into the cache
		putFieldsIntoCache(clazz, fields);

		return fields;
	}

	/**
	 * Sets given property as object's field, trying to fetch the property's value of type that matches the
	 * field best. Fields of the following types are supported by this method:
	 * <ul>
	 * <li>{@link Boolean}</li>
	 * <li>{@link Calendar}</li>
	 * <li>{@link Double}</li>
	 * <li>{@link InputStream}</li>
	 * <li>{@link Long}</li>
	 * <li>{@link Node}</li>
	 * <li>{@link String}</li>
	 * </ul>
	 * 
	 * If the property cannot be converted to field's type, a {@link RepositoryException} will be thrown. If
	 * given field cannot be assigned from any of the supported types, an {@link IllegalArgumentException}
	 * will be thrown.
	 * 
	 * @param property property to be set as field's value
	 * @param field field to be set
	 * @param object target of the method, must contain the field
	 * 
	 * @throws RepositoryException If the property cannot be converted to field's type, or if it's
	 * multi-valued, or other repository specific error occurs.
	 * @throws IllegalAccessException if field is not accessible.
	 * @throws IllegalArgumentException if field cannot be assigned from any of the supported types.
	 */
	public static void setObjectField(Property property, Field field, Object object)
			throws RepositoryException, IllegalAccessException {
		Value value = property.getValue();

		Class<?> declaringClass = field.getDeclaringClass();
		if (declaringClass.isAssignableFrom(Boolean.class)) {
			field.set(object, value.getBoolean());
		} else if (declaringClass.isAssignableFrom(Calendar.class)) {
			field.set(object, value.getDate());
		} else if (declaringClass.isAssignableFrom(Double.class)) {
			field.set(object, value.getDouble());
		} else if (declaringClass.isAssignableFrom(InputStream.class)) {
			field.set(object, value.getBinary().getStream());
		} else if (declaringClass.isAssignableFrom(Long.class)) {
			field.set(object, value.getLong());
		} else if (declaringClass.isAssignableFrom(String.class)) {
			field.set(object, value.getString());
		} else {
			// cannot assign the field
			throw new IllegalArgumentException();
		}
	}

	private static Field[] getFieldsFromCache(Class<?> clazz) {
		SoftReference<Field[]> softReferenceFields = fieldsCache.get(clazz);
		Field[] fields = null;
		if (softReferenceFields != null) {
			fields = softReferenceFields.get();
		}
		return fields;
	}

	private static void putFieldsIntoCache(Class<?> clazz, Field[] fields) {
		fieldsCache.put(clazz, new SoftReference<Field[]>(fields));
	}
}
