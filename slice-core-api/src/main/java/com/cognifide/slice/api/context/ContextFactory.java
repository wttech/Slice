/*-
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

package com.cognifide.slice.api.context;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.sling.api.resource.ResourceResolver;

/**
 * @author Rafał Malinowski
 * 
 * Factory for creating Context instances.
 */
public interface ContextFactory {

	public static final String COMMON_CONTEXT_NAME = ContextFactory.class.getName() + ".commonContext";

	/**
	 * Create Context from request and response objects. Returned Context will delegate it stored/reads to
	 * request object.
	 * 
	 * @deprecated Use method with explicit injector name
	 * {@code ContextFactory#getServletRequestContext(String, ServletRequest, ServletResponse)}
	 */
	@Deprecated
	Context getServletRequestContext(ServletRequest request, ServletResponse response);

	/**
	 * Create Context from request and response objects. Returned Context will delegate it stored/reads to
	 * request object. Binding are stored per-injector.
	 * 
	 * Returned Context will contain entries for ServletRequest.class, ServletResponse.class and
	 * Resource.class with RequestedResourcePath attribute.
	 */
	Context getServletRequestContext(String injectorName, ServletRequest request, ServletResponse response);

	/**
	 * Create Context from resourceResolver object. Returned Context will store all objects in itself - it
	 * will behave as per-thread context.
	 * 
	 * Returned Context will contain entry for ResourceResolver.class. No ServletRequest.class or
	 * ServletResponse.class will be available for injector.
	 */
	Context getResourceResolverContext(ResourceResolver resourceResolver);

}
