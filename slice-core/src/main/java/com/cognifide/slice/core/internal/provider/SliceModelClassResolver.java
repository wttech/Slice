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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.classloader.DynamicClassLoaderManager;

import com.cognifide.slice.api.model.ModelClassResolver;
import com.cognifide.slice.api.provider.ComponentDefinitionResolver;

@Component
@Service
public class SliceModelClassResolver implements ModelClassResolver {

	@Reference(cardinality = ReferenceCardinality.OPTIONAL_UNARY)
	private ComponentDefinitionResolver componentDefinitionResolver;

	@Reference
	private DynamicClassLoaderManager dynamicClassLoaderManager;

	@Override
	public Class<?> getModelClass(Resource resource) throws ClassNotFoundException {
		final Map<String, Object> definition = getDefinition(resource);
		if (definition != null) { // definition may not be available for types like cq:Page
			final String className = (String) definition.get("slice:model");
			if (StringUtils.isBlank(className)) {
				return null;
			} else {
				return dynamicClassLoaderManager.getDynamicClassLoader().loadClass(className);
			}
		}
		return null;
	}

	private Map<String, Object> getDefinition(Resource resource) throws ClassNotFoundException {
		final Map<String, Object> definition;
		if (componentDefinitionResolver == null) {
			definition = getDefinitionWithResolver(resource);
		} else {
			definition = componentDefinitionResolver.getComponentDefinition(resource.getResourceType());
		}
		return definition;
	}

	private Map<String, Object> getDefinitionWithResolver(Resource resource) {
		com.day.cq.wcm.api.components.Component component = resource
				.adaptTo(com.day.cq.wcm.api.components.Component.class);
		if (component != null) {
			Map<String, Object> values = component.adaptTo(ValueMap.class);
			if (values != null) {
				return new HashMap<String, Object>(values);
			}
		}
		return null;
	}
}
