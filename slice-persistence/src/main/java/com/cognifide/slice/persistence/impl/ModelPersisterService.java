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

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;

import com.cognifide.slice.persistence.api.ModelPersister;
import com.cognifide.slice.persistence.api.SerializerFacade;

@Component(immediate = true)
@Service(ModelPersister.class)
public class ModelPersisterService implements ModelPersister {

	@Reference
	private SerializerFacade facade;

	@Override
	public void persist(Object object, Resource destinationResource) throws PersistenceException {
		persist(object, destinationResource.getName(), destinationResource.getParent());
	}

	@Override
	public void persist(Object object, String childName, Resource parent) throws PersistenceException {
		final DefaultSerializerContext ctx = new DefaultSerializerContext();
		facade.serializeObject(childName, object, parent, ctx);
	}
}
