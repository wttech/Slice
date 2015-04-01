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

package com.cognifide.slice.core.internal.adapter;

import org.apache.sling.api.adapter.AdapterFactory;
import org.apache.sling.api.resource.Resource;

import com.cognifide.slice.api.context.*;
import com.cognifide.slice.api.injector.InjectorWithContext;
import com.cognifide.slice.api.provider.ModelProvider;
import com.cognifide.slice.core.internal.injector.InjectorWithContextImpl;
import com.google.inject.Injector;

public class SliceAdapterFactory implements AdapterFactory {

	private final Injector injector;

	private final String injectorName;

	private final RequestContextProvider requestContextProvider;

	public SliceAdapterFactory(String injectorName, Injector injector,
			RequestContextProvider requestContextProvider) {
		this.injector = injector;
		this.requestContextProvider = requestContextProvider;
		this.injectorName = injectorName;
	}

	@Override
	public <AdapterType> AdapterType getAdapter(Object adaptable, Class<AdapterType> type) {
		if (!(adaptable instanceof Resource)) {
			return null;
		}
		Resource resource = (Resource) adaptable;

		InjectorWithContext injector = getInjector(resource);
		try {
			ModelProvider modelProvider = injector.getInstance(ModelProvider.class);
			return modelProvider.get(type, resource);
		} finally {
			injector.popContextProvider();
		}
	}

	private InjectorWithContext getInjector(Resource resource) {
		InjectorWithContext injectorWithContext = new InjectorWithContextImpl(injector);
		ContextProvider contextProvider = requestContextProvider.getContextProvider(injectorName);
		if (contextProvider == null) {
			ContextFactory factory = injectorWithContext.getInstance(ContextFactory.class);
			Context context = factory.getResourceResolverContext(resource.getResourceResolver());
			contextProvider = new ConstantContextProvider(context);
		}
		injectorWithContext.pushContextProvider(contextProvider);
		return injectorWithContext;
	}

}
