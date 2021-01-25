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

package com.cognifide.slice.core.internal.context;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.cognifide.slice.api.context.Context;
import com.cognifide.slice.api.context.ContextFactory;
import com.cognifide.slice.api.qualifier.RequestedResource;
import com.cognifide.slice.core.internal.context.MapContext;
import com.cognifide.slice.core.internal.context.ServletRequestContext;
import com.cognifide.slice.util.ServletRequestResponseUtil;
import com.google.inject.Key;

/**
 * @author Rafał Malinowski
 * 
 * Helper class for creating Context instances.
 */
public class SliceContextFactory implements ContextFactory {

	/**
	 * Create common context from request and response objects. Returned Context will delegate it stored/reads
	 * to request object.
	 * 
	 * @deprecated Use method with explicit injector name
	 * {@code ContextFactory#getServletRequestContext(String, ServletRequest, ServletResponse)}
	 */
	@Override
	public Context getServletRequestContext(ServletRequest request, ServletResponse response) {
		return getServletRequestContext(COMMON_CONTEXT_NAME, request, response);
	}

	/**
	 * Create Context from request and response objects. Returned Context will delegate it stored/reads to
	 * request object - it will behave a bit like RequestScope from Slice 1.0.
	 * 
	 * Returned Context will contain entries for ServletRequest.class, ServletResponse.class and
	 * Resource.class with RequestedResourcePath attribute.
	 */
	@Override
	public Context getServletRequestContext(final String injectorName, final ServletRequest request,
			final ServletResponse response) {
		if (null == request || null == response) {
			throw new IllegalArgumentException("Request and response can't be null");
		}

		final Context context = new ServletRequestContext(injectorName, request);
		context.put(Key.get(ServletRequest.class), request);
		context.put(Key.get(ServletResponse.class), response);

		final Resource requestedResource = getRequestedResource(request);
		context.put(Key.get(Resource.class, RequestedResource.class), requestedResource);

		return context;
	}

	private Resource getRequestedResource(final ServletRequest request) {
		return ServletRequestResponseUtil.getSlingHttpServletRequest(request).getResource();
	}

	/**
	 * Create Context from resourceResolver object. Returned Context will store all objects in itself - it
	 * will behave as per-thread context.
	 * 
	 * Returned Context will contain entry for ResourceResolver.class. No ServletRequest.class or
	 * ServletResponse.class will be available for injector.
	 */
	@Override
	public Context getResourceResolverContext(final ResourceResolver resourceResolver) {
		final MapContext mapContex = new MapContext();
		mapContex.put(Key.get(ResourceResolver.class), resourceResolver);

		return mapContex;
	}
}
