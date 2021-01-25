/*-
 * #%L
 * Slice - Persistence
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
package com.cognifide.slice.persistence.impl.serializer;

import java.lang.reflect.Field;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.cognifide.slice.persistence.api.SerializerContext;
import com.cognifide.slice.persistence.api.serializer.FieldSerializer;

public abstract class ChildrenSerializer implements FieldSerializer {

	@Override
	public void serialize(Field field, String childName, Object fieldValue, Resource parent, SerializerContext ctx) throws PersistenceException {
		final ResourceResolver resolver = parent.getResourceResolver();

		if (fieldValue == null) {
			removeChildren(childName, parent, resolver);
		} else {
			createOrUpdateChildren(childName, fieldValue, parent, ctx, resolver);
		}
	}

	protected abstract void createChildren(String childName, Object fieldValue, SerializerContext ctx, Resource child)
			throws PersistenceException;

	protected String generateChildName(String name, int index) {
		return String.format("%s_%d", name, index);
	}

	protected void removeChildren(String childName, Resource parent, ResourceResolver resolver)
			throws PersistenceException {
		Resource child = parent.getChild(childName);
		if (child != null) {
			resolver.delete(child);
		}
	}

	protected void createOrUpdateChildren(String childName, Object fieldValue, Resource parent,
			SerializerContext ctx, ResourceResolver resolver) throws PersistenceException {
		Resource child = parent.getChild(childName);
		if (child == null) {
			child = resolver.create(parent, childName, ctx.getInitialProperties());
		} else {
			removeExistingChildren(child);
		}

		createChildren(childName, fieldValue, ctx, child);
	}

	protected void removeExistingChildren(Resource parent) throws PersistenceException {
		final ResourceResolver resourceResolver = parent.getResourceResolver();
		for (Resource resource : parent.getChildren()) {
			resourceResolver.delete(resource);
		}
	}
}
