/*-
 * #%L
 * Slice - Core API
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

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import aQute.bnd.annotation.ProviderType;

/**
 * 
 * Factory for creating Context instances. It can create context for the following:
 * <ul>
 * <li>Request - it stores request and response objects in its context</li>
 * <li>ResourceResolver - it stores resource resolver in its context</li>
 * </ul>
 * 
 * @author Rafał Malinowski
 */
@ProviderType
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
	 * Create Context from request and response objects.
	 * 
	 * Returned Context will contain entries for {@link ServletRequest}, {@link ServletResponse} and
	 * {@link Resource} read from request (ResourceResolver is not stored in the context, but it can be read
	 * from resource stored in context). All of the values from this context are kept in specified request.
	 * 
	 * @param injectorName name of injector for which a context will be created
	 * @param request servlet request to be stored in context and which will be used to keep all values from
	 * this context
	 * @param response servlet response to be stored in context
	 * @return context containing request, response and resource read from request
	 */
	Context getServletRequestContext(String injectorName, ServletRequest request, ServletResponse response);

	/**
	 * Create Context from specified resourceResolver object. All of the values from this context are kept in
	 * context itself (as a per-thread map)
	 * 
	 * Returned Context will contain entry for {@link ResourceResolver}. No {@link ServletRequest} nor
	 * {@link ServletResponse} will be available in this context for use in injector.
	 * 
	 * @param resourceResolver {@link ResourceResolver} to be stored in the context
	 * @return context containing specified resource resolver
	 */
	Context getResourceResolverContext(ResourceResolver resourceResolver);

}
