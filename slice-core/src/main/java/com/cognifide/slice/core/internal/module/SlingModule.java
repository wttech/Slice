/*-
 * #%L
 * Slice - Core
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2012 - 2014 Cognifide Limited
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

package com.cognifide.slice.core.internal.module;

import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.commons.classloader.DynamicClassLoaderManager;
import org.ops4j.peaberry.Peaberry;

import com.cognifide.slice.api.context.ContextScope;
import com.cognifide.slice.api.qualifier.Extension;
import com.cognifide.slice.api.qualifier.SelectorString;
import com.cognifide.slice.api.qualifier.Selectors;
import com.cognifide.slice.api.qualifier.Suffix;
import com.cognifide.slice.api.scope.ContextScoped;
import com.cognifide.slice.commons.module.ContextScopeModule;
import com.cognifide.slice.core.internal.provider.CurrentResourceProvider;
import com.cognifide.slice.util.ServletRequestResponseUtil;
import com.google.inject.Key;
import com.google.inject.Provides;

public final class SlingModule extends ContextScopeModule {

	public SlingModule(final ContextScope contextScope) {
		super(contextScope);
	}

	@Override
	protected void configure() {
		bindToContextScope(Key.get(ServletRequest.class));
		bindToContextScope(Key.get(ServletResponse.class));
		bind(Resource.class).toProvider(CurrentResourceProvider.class);
		bind(DynamicClassLoaderManager.class).toProvider(Peaberry.service(DynamicClassLoaderManager.class).single().direct());
	}

	@Provides
	@ContextScoped
	public SlingHttpServletRequest getSlingHttpServletRequest(final ServletRequest request) {
		return ServletRequestResponseUtil.getSlingHttpServletRequest(request);
	}

	@Provides
	@ContextScoped
	public SlingHttpServletResponse getSlingHttpServletResponse(final ServletResponse response) {
		return ServletRequestResponseUtil.getSlingHttpServletResponse(response);
	}

	/**
	 * Returns {@link ResourceResolver}. If using {@link MapContext} obtained by
	 * {@link SliceContextFactory#getResourceResolverContext(ResourceResolver)}, resourceResolver will be returned
	 * by context, not by this method
	 */
	@Provides
	@ContextScoped
	public ResourceResolver getResourceResolver(final SlingHttpServletRequest request) {
		return request.getResourceResolver();
	}

	@Provides
	public RequestPathInfo getRequestPathInfo(final SlingHttpServletRequest request) {
		return request.getRequestPathInfo();
	}

	@Provides
	@Extension
	public String getExtension(final RequestPathInfo requestPathInfo) {
		return requestPathInfo.getExtension();
	}

	@Provides
	@Suffix
	public String getSuffix(final RequestPathInfo requestPathInfo) {
		return requestPathInfo.getSuffix();
	}

	@Provides
	@Selectors
	public String[] getSelectors(final RequestPathInfo requestPathInfo) {
		return requestPathInfo.getSelectors();
	}

	@Provides
	@Selectors
	public List<String> getSelectorsAsList(final RequestPathInfo requestPathInfo) {
		return Arrays.asList(requestPathInfo.getSelectors());
	}

	@Provides
	@SelectorString
	public String getSelectorsString(final RequestPathInfo requestPathInfo) {
		return requestPathInfo.getSelectorString();
	}

}
