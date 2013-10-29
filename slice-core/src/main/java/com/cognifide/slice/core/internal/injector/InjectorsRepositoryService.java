package com.cognifide.slice.core.internal.injector;

//@formatter:off
/*
* #%L
* Slice - Core API
* $Id:$
* $HeadURL:$
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
//@formatter:on

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.api.injector.InjectorConfig;
import com.cognifide.slice.api.injector.InjectorWithContext;
import com.cognifide.slice.api.injector.InjectorsRepository;
import com.cognifide.slice.util.InjectorNameUtil;
import com.google.inject.Injector;

/**
 * @author Witold Szczerba
 * @author Rafał Malinowski
 * @author Jan Kuźniak
 * @class InjectorsRepositoryService
 */
@Component(immediate = true)
@Service
// @formatter:off
@Properties({
	@Property(name = Constants.SERVICE_DESCRIPTION, value = "Repository of all Slice injectors."),
	@Property(name = Constants.SERVICE_VENDOR, value = "Cognifide")
})
// @formatter:on
public final class InjectorsRepositoryService implements InjectorsRepository {

	private static final Logger LOG = LoggerFactory.getLogger(InjectorsRepositoryService.class);

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	@Reference(cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, referenceInterface = InjectorConfig.class, //
	policy = ReferencePolicy.DYNAMIC)
	private final InjectorHierarchy injectors = new InjectorHierarchy();

	@Override
	public InjectorWithContext getInjector(final String injectorName) {
		InjectorWithContextImpl injectorWithContext = null;
		Injector injector = injectors.getInjectorByName(injectorName);
		if (injector != null) {
			injectorWithContext = new InjectorWithContextImpl(injector);
		}
		return injectorWithContext;
	}

	@Override
	public InjectorWithContext getInjectorForResource(Resource resource) {
		String injectorName = InjectorNameUtil.getForResource(resource);
		return getInjector(injectorName);
	}

	@Override
	public InjectorWithContext getInjectorForResource(String resourcePath) {
		InjectorWithContext injector = null;
		try {
			injector = getInjectorForResourceAsAdmin(resourcePath);
		} catch (LoginException e) {
			// yikes! I can no longer login to repostiory with "null" credentials!
			LOG.error("Unexpected: could not access administrative resource resolver");
		}
		return injector;
	}

	private InjectorWithContext getInjectorForResourceAsAdmin(String resourcePath) throws LoginException {
		ResourceResolver resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
		Resource resource = resourceResolver.getResource(resourcePath);
		InjectorWithContext injector = getInjectorForResource(resource);
		resourceResolver.close();
		return injector;
	}

	protected void bindInjectors(final InjectorConfig config) {
		injectors.registerInjector(config);
	}

	protected void unbindInjectors(final InjectorConfig config) {
		injectors.unregisterInjector(config);
	}
}
