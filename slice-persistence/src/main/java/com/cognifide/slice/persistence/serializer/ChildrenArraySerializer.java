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

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;

import com.cognifide.slice.mapper.annotation.Children;
import com.cognifide.slice.persistence.api.FieldSerializer;
import com.cognifide.slice.persistence.api.SerializerContext;

public class ChildrenArraySerializer implements FieldSerializer {

	@Override
	public boolean accepts(Field field) {
		return field.isAnnotationPresent(Children.class) && field.getType().isArray();
	}

	@Override
	public void serialize(Field field, String childName, Object fieldValue, Resource parent,
			SerializerContext ctx) throws PersistenceException {
		Resource child = parent.getChild(childName);
		if (child == null) {
			child = parent.getResourceResolver().create(parent, childName,
					SerializerContext.getInitialProperties());
		}
		final int arrayLength = Array.getLength(fieldValue);
		for (int i = 0; i < arrayLength; i++) {
			ctx.getFacade().serialize(String.format("%s_%d", childName, i + 1), Array.get(fieldValue, i),
					child, ctx);
		}
	}

	@Override
	public int getRank() {
		return 0;
	}
}