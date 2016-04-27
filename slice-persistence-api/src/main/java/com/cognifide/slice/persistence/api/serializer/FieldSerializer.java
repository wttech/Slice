/*-
 * #%L
 * Slice - Persistence API
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
package com.cognifide.slice.persistence.api.serializer;

import java.lang.reflect.Field;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;

import com.cognifide.slice.persistence.api.SerializerContext;

import aQute.bnd.annotation.ConsumerType;

/**
 * This type handles saving a single object field into repository. It has access to the reflection
 * {@link Field} value, so it can read its name, type, etc.
 *
 * @since 4.3
 */
@ConsumerType
public interface FieldSerializer extends Serializer {

	/**
	 * Serializer will return {@code true} if it's able to handle such field.
	 */
	boolean accepts(Field field);

	/**
	 * Serializer an object into repository
	 *
	 * @param field Field in the Slice model class
	 * @param propertyName Name of the field
	 * @param fieldValue Value of the field
	 * @param parent Resource to save the field
	 * @param ctx Serialization context
	 * @throws PersistenceException
	 */
	void serialize(Field field, String propertyName, Object fieldValue, Resource parent,
			SerializerContext ctx) throws PersistenceException;

}
