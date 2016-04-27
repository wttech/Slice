/*-
 * #%L
 * Slice - Core
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
package com.cognifide.slice.core.internal.provider;

import javax.script.Bindings;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.scripting.api.BindingsValuesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.api.model.ModelClassResolver;

@Component
@Service
public class SliceBindingsProvider implements BindingsValuesProvider {

	private static final Logger LOG = LoggerFactory.getLogger(SliceBindingsProvider.class);

	@Reference
	private ModelClassResolver modelClassNameResolver;

	@Override
	public void addBindings(Bindings bindings) {
		final Resource resource = (Resource) bindings.get("resource");
		if (resource == null) {
			return;
		}
		try {
			final Class<?> modelClass = modelClassNameResolver.getModelClass(resource);
			if (modelClass != null) {
				bindings.put("model", resource.adaptTo(modelClass));
			}
		} catch (ClassNotFoundException e) {
			LOG.error("Can't resolver Slice model class", e);
		}
	}

}
