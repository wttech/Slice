/*-
 * #%L
 * Slice - Core
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

package com.cognifide.slice.core.internal.adapter;

import org.apache.sling.api.adapter.AdapterFactory;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.api.context.ConstantContextProvider;
import com.cognifide.slice.api.context.Context;
import com.cognifide.slice.api.context.ContextFactory;
import com.cognifide.slice.api.context.ContextProvider;
import com.cognifide.slice.api.context.RequestContextProvider;
import com.cognifide.slice.api.injector.InjectorWithContext;
import com.cognifide.slice.api.injector.InjectorsRepository;
import com.cognifide.slice.api.provider.ModelProvider;

public class SliceAdapterFactory implements AdapterFactory {

	private final static Logger LOG = LoggerFactory.getLogger(SliceAdapterFactory.class);

	private final String injectorName;

	public SliceAdapterFactory(String injectorName) {
		this.injectorName = injectorName;
	}

	@Override
	public <AdapterType> AdapterType getAdapter(Object adaptable, Class<AdapterType> type) {
		if (!(adaptable instanceof Resource)) {
			return null;
		}
		Resource resource = (Resource) adaptable;

		InjectorWithContext injector = getInjector(resource);
		if (injector != null) {
			try {
				ModelProvider modelProvider = injector.getInstance(ModelProvider.class);
				return modelProvider.get(type, resource);
			} finally {
				injector.popContextProvider();
			}
		} else {
			LOG.warn("Trying to adapt resource to {}, but suitable injector (named {}) not found!",
					new Object[] { type.toString(), injectorName });
			return null;
		}
	}

	private InjectorWithContext getInjector(Resource resource) {
		ResourceResolver resourceResolver = resource.getResourceResolver();
		InjectorsRepository repository = resourceResolver.adaptTo(InjectorsRepository.class);
		if (repository == null) {
			return null;
		}
		InjectorWithContext injector = repository.getInjector(injectorName);
		if (injector != null) {
			ContextProvider contextProvider = getContextProviderFromRequest(resourceResolver);
			if (contextProvider == null) {
				contextProvider = getContextProviderFromResourceResolver(resourceResolver, injector);
			}
			injector.pushContextProvider(contextProvider);
		}
		return injector;
	}

	private ContextProvider getContextProviderFromRequest(ResourceResolver resourceResolver) {
		RequestContextProvider requestContextProvider = resourceResolver
				.adaptTo(RequestContextProvider.class);
		ContextProvider contextProvider = null;
		if (requestContextProvider != null) {
			contextProvider = requestContextProvider.getContextProvider(injectorName);
		}
		return contextProvider;
	}

	private ContextProvider getContextProviderFromResourceResolver(ResourceResolver resourceResolver,
			InjectorWithContext injector) {
		ContextFactory factory = injector.getInstance(ContextFactory.class);
		Context context = factory.getResourceResolverContext(resourceResolver);
		return new ConstantContextProvider(context);
	}

}
