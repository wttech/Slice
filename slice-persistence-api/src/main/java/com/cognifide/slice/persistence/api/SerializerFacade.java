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
package com.cognifide.slice.persistence.api;

import java.lang.reflect.Field;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;

import com.cognifide.slice.persistence.api.serializer.FieldSerializer;
import com.cognifide.slice.persistence.api.serializer.ObjectSerializer;

import aQute.bnd.annotation.ProviderType;

/**
 * A service providing access to all registered serializers.
 *
 * @since 4.3
 */
@ProviderType
public interface SerializerFacade {

	/**
	 * See {@link ObjectSerializer#serialize(String, Object, Resource, SerializerContext)}.
	 */
	void serializeObject(String objectName, Object object, Resource parent, SerializerContext ctx)
			throws PersistenceException;

	/**
	 * See {@link FieldSerializer#serialize(Field, String, Object, Resource, SerializerContext)}.
	 */
	void serializeField(Field field, String propertyName, Object fieldValue, Resource parent,
			SerializerContext ctx) throws PersistenceException;

}
