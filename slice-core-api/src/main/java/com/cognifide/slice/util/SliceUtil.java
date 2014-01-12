/*-
 * #%L
 * Slice - Core
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

package com.cognifide.slice.util;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.cognifide.slice.api.context.Context;
import com.cognifide.slice.api.context.ContextFactory;
import com.cognifide.slice.api.context.ContextProvider;
import com.cognifide.slice.api.context.RequestContextProvider;
import com.cognifide.slice.api.injector.InjectorWithContext;
import com.cognifide.slice.api.injector.InjectorsRepository;

/**
 * This util class provides useful tools for getting Slice injectors and injecting models. Each public method
 * has two versions: first one gets Sling request and response, the other requires passing resource resolver.
 * 
 * @author Tomasz Rekawek
 * 
 */
public final class SliceUtil {
	private SliceUtil() {
	}

	/**
	 * Returns injector created in the context of given request and resource. Injector should be closed after
	 * usage. Sample usage in Java 7:
	 * 
	 * <pre>
	 * try (InjectorWithContext injector = SliceUtil.getInjector(&quot;myApp&quot;, request, response)) {
	 * 	ModelProvider modelProvider = injector.getInstance(ModelProvider.class);
	 * 	SimpleModel simpleModel = modelProvider.get(SimpleModel.class, resource);
	 * 	// do something clever with the model
	 * }
	 * </pre>
	 * 
	 * In Java 6 you can use try-finally idiom:
	 * 
	 * <pre>
	 * InjectorWithContext injector = SliceUtil.getInjector(&quot;myApp&quot;, request, response)
	 * try {
	 * 	ModelProvider modelProvider = injector.getInstance(ModelProvider.class);
	 * 	SimpleModel simpleModel = modelProvider.get(SimpleModel.class, resource);
	 * 	// do something clever with the model
	 * } finally {
	 * 	injector.close();
	 * }
	 * </pre>
	 * 
	 * @param injectorName Name of the desired injector
	 * @return Created injector
	 */
	public static InjectorWithContext getInjector(String injectorName, SlingHttpServletRequest request) {
		InjectorWithContext injector = request.adaptTo(InjectorsRepository.class).getInjector(injectorName);
		RequestContextProvider requestContextProvider = request.adaptTo(RequestContextProvider.class);
		injector.pushContextProvider(requestContextProvider.getContextProvider(injectorName));
		return injector;
	}

	/**
	 * Returns injector created in the context of given resource resolver. Injector should be closed after
	 * usage. See {@link #getInjector(String, SlingHttpServletRequest, SlingHttpServletResponse)} for more
	 * info.
	 * 
	 * @param injectorName Name of the desired injector
	 * @return Created injector
	 */
	public static InjectorWithContext getInjector(String injectorName, ResourceResolver resolver) {
		InjectorWithContext injector = resolver.adaptTo(InjectorsRepository.class).getInjector(injectorName);
		ContextFactory factory = injector.getInstance(ContextFactory.class);
		Context context = factory.getResourceResolverContext(resolver);
		injector.pushContextProvider(new ConstantContextProvider(context));
		return injector;
	}

	/**
	 * Creates {@link com.cognifide.slice.mapper.annotation.SliceResource} model from given resource. This
	 * method is useful if you need to get a single model. However, invoking it to get multiple models, one
	 * after another won't be as effective as creating injector and {@code ModelProvider} manually. Sample
	 * usage:
	 * 
	 * <pre>
	 * SimpleModel model = SliceUtil.injectModel(SimpleModel.class, myResource);
	 * // do something clever with the model
	 * </pre>
	 * 
	 * @deprecated Use {@code resource.adaptTo(Model.class)} instead.
	 * @param type Model class
	 * @param resource Resource to map
	 * @return Injected and mapped model
	 */
	@Deprecated
	public static <T> T injectModel(Class<T> type, Resource resource) {
		return resource.adaptTo(type);
	}

	/**
	 * Simple context provider returning always the same context.
	 */
	public static final class ConstantContextProvider implements ContextProvider {
		private final Context context;

		public ConstantContextProvider(Context context) {
			this.context = context;
		}

		@Override
		public Context getContext() {
			return context;
		}
	}
}
