/*-
 * #%L
 * Slice - Core API
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

package com.cognifide.slice.util;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;

import com.cognifide.slice.api.context.ConstantContextProvider;
import com.cognifide.slice.api.context.Context;
import com.cognifide.slice.api.context.ContextFactory;
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
public final class InjectorUtil {
	private InjectorUtil() {
	}

	/**
	 * Returns injector created in the context of given request and resource. Injector should be closed after
	 * usage. Sample usage in Java 7:
	 * 
	 * <pre>
	 * try (InjectorWithContext injector = InjectorUtil.getInjector(&quot;myApp&quot;, request)) {
	 * 	ModelProvider modelProvider = injector.getInstance(ModelProvider.class);
	 * 	SimpleModel simpleModel = modelProvider.get(SimpleModel.class, resource);
	 * 	// do something clever with the model
	 * }
	 * </pre>
	 * 
	 * In Java 6 you can use try-finally idiom:
	 * 
	 * <pre>
	 * InjectorWithContext injector = InjectorUtil.getInjector(&quot;myApp&quot;, request)
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
	 * @param request Request used to provide context
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
	 * usage. See {@link #getInjector(String, SlingHttpServletRequest)} for more info. <br>
	 * <br>
	 * Please notice that Sling request and response objects won't be bound to the context of the created
	 * injector. This method is meant to be used to provide Slice injector in OSGi components, where you have
	 * no request.
	 * 
	 * @param injectorName Name of the desired injector
	 * @param resolver Resolver used to provide context
	 * @return Created injector
	 */
	public static InjectorWithContext getInjector(String injectorName, ResourceResolver resolver) {
		InjectorWithContext injector = resolver.adaptTo(InjectorsRepository.class).getInjector(injectorName);
		ContextFactory factory = injector.getInstance(ContextFactory.class);
		Context context = factory.getResourceResolverContext(resolver);
		injector.pushContextProvider(new ConstantContextProvider(context));
		return injector;
	}
}
