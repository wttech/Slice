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
import org.apache.felix.scr.annotations.ReferencePolicyOption;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.NonExistingResource;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.classloader.DynamicClassLoaderManager;

import com.cognifide.slice.api.model.ModelClassResolver;
import com.cognifide.slice.api.provider.ComponentDefinitionProvider;
import com.cognifide.slice.api.provider.ComponentDefinitionResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Service
public class SliceModelClassResolver implements ModelClassResolver {

	private static final Logger LOG = LoggerFactory.getLogger(SliceModelClassResolver.class);

	@Reference(cardinality = ReferenceCardinality.OPTIONAL_UNARY,policyOption = ReferencePolicyOption.GREEDY)
	private ComponentDefinitionResolver componentDefinitionResolver;

	@Reference(cardinality = ReferenceCardinality.OPTIONAL_UNARY, policyOption = ReferencePolicyOption.GREEDY)
	private ComponentDefinitionProvider componentDefinitionProvider;

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	@Reference
	private DynamicClassLoaderManager dynamicClassLoaderManager;

	@Override
	public Class<?> getModelClass(String resourceType) throws ClassNotFoundException {
		final Map<String, Object> definition = getDefinition(resourceType);
		return loadDefinition(definition);
	}

	@Override
	public Class<?> getModelClass(Resource resource) throws ClassNotFoundException {
		if (resource == null || resource instanceof NonExistingResource) {
			return null;
		}
		final Map<String, Object> definition = getDefinition(resource);
		return loadDefinition(definition);
	}

	private Class<?> loadDefinition(final Map<String, Object> definition) throws ClassNotFoundException {
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
		if (componentDefinitionProvider == null) {
			String resourceType = resource.getResourceType();
			definition = getDefinition(resourceType);
		} else {
			definition = componentDefinitionProvider.getComponentDefinition(resource);
		}
		return definition;
	}

	/**
	 * The old way, to be removed in Slice 5
	 */
	private Map<String, Object> getDefinition(String resourceType) {
		final Map<String, Object> definition;
		if (componentDefinitionResolver == null) {
			definition = getDefinitionWithResolver(resourceType);
		} else {
			definition = componentDefinitionResolver.getComponentDefinition(resourceType);
		}
		return definition;
	}

	private Map<String, Object> getDefinitionWithResolver(String resourceType) {
		ResourceResolver resolver = null;
		try {
			/*
				FIXME: This method has been deprecated as of Sling 2.4 (bundle version 2.5.0)
							 consider replacing it with getServiceResourceResolver
			 */
			resolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
			final Resource componentDefinition = resolver.getResource(resourceType);
			if (componentDefinition != null) {
				Map<String, Object> values = componentDefinition.adaptTo(ValueMap.class);
				if (values != null) {
					return new HashMap<String, Object>(values);
				}
			}
			return null;
		} catch (LoginException e) {
			LOG.error("Failed to load definition due to insufficient permissions.", e);
			return null;
		} finally {
			if (resolver != null) {
				resolver.close();
			}
		}
	}
}
