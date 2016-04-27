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
package com.cognifide.slice.persistence.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;

import com.cognifide.slice.persistence.api.SerializerContext;
import com.cognifide.slice.persistence.api.SerializerFacade;
import com.cognifide.slice.persistence.api.serializer.FieldSerializer;
import com.cognifide.slice.persistence.api.serializer.FieldSerializerAdapter;
import com.cognifide.slice.persistence.api.serializer.ObjectSerializer;
import com.cognifide.slice.persistence.api.serializer.Serializer;
import com.google.inject.Inject;

public class SerializerFacadeService implements SerializerFacade {

	private final List<Serializer> serializers;

	private static final ObjectSerializer EMPTY_OBJECT_SERIALIZER = new ObjectSerializer() {

		@Override
		public int getPriority() {
			return 0;
		}

		@Override
		public boolean accepts(Class<?> objectClass) {
			return false;
		}

		@Override
		public void serialize(String objectName, Object object, Resource parent, SerializerContext ctx)
				throws PersistenceException {
			// do nothing
		}
	};

	private static final FieldSerializer EMPTY_FIELD_SERIALIZER = new FieldSerializerAdapter(
			EMPTY_OBJECT_SERIALIZER);

	private static final Comparator<Serializer> PRIORITY_SERIALIZER_COMPARATOR = new Comparator<Serializer>() {
		@Override
		public int compare(Serializer s1, Serializer s2) {
			return Integer.valueOf(s2.getPriority()).compareTo(s1.getPriority());
		}
	};

	@Inject
	public SerializerFacadeService(Set<Serializer> serializers) {
		this.serializers = new ArrayList<Serializer>(serializers);
		Collections.sort(this.serializers, PRIORITY_SERIALIZER_COMPARATOR);
	}

	@Override
	public void serializeObject(String objectName, Object object, Resource parent, SerializerContext ctx)
			throws PersistenceException {
		final ObjectSerializer serializer = findObjectSerializer(object.getClass());
		serializer.serialize(objectName, object, parent, ctx);
	}

	@Override
	public void serializeField(Field field, String propertyName, Object fieldValue, Resource parent,
			SerializerContext ctx) throws PersistenceException {
		final FieldSerializer serializer = findFieldSerializer(field);
		serializer.serialize(field, propertyName, fieldValue, parent, ctx);
	}

	private FieldSerializer findFieldSerializer(Field field) {
		for (Object o : serializers) {
			final FieldSerializer s;
			if (o instanceof FieldSerializer) {
				s = (FieldSerializer) o;
			} else if (o instanceof ObjectSerializer) {
				s = new FieldSerializerAdapter((ObjectSerializer) o);
			} else {
				continue;
			}
			if (s.accepts(field)) {
				return s;
			}
		}
		return EMPTY_FIELD_SERIALIZER;
	}

	private ObjectSerializer findObjectSerializer(Class<?> clazz) {
		for (Object o : serializers) {
			if (!(o instanceof ObjectSerializer)) {
				continue;
			}
			final ObjectSerializer s = (ObjectSerializer) o;
			if (s.accepts(clazz)) {
				return s;
			}
		}
		return EMPTY_OBJECT_SERIALIZER;
	}
}
