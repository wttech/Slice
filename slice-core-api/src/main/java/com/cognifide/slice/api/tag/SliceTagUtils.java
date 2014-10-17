package com.cognifide.slice.api.tag;

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

import javax.servlet.ServletRequest;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;

import com.cognifide.slice.api.context.ContextFactory;
import com.cognifide.slice.api.context.ContextProvider;
import com.cognifide.slice.api.context.RequestContextProvider;
import com.cognifide.slice.api.injector.InjectorWithContext;
import com.cognifide.slice.api.injector.InjectorsRepository;
import com.cognifide.slice.api.provider.ModelProvider;
import com.cognifide.slice.util.InjectorNameUtil;

public final class SliceTagUtils {

	private SliceTagUtils() {
		// hidden constructor
	}

	public static <T> T getFromCurrentPath(final PageContext pageContext, final Class<T> type) {
		return getFromCurrentPath(pageContext, type, null);
	}

	public static <T> T getFromCurrentPath(final PageContext pageContext, final Class<T> type,
			final String appName) {
		final SlingHttpServletRequest request = SliceTagUtils.slingRequestFrom(pageContext);
		final InjectorsRepository injectorsRepository = SliceTagUtils.injectorsRepositoryFrom(pageContext);
		final RequestContextProvider requestContextProvider = SliceTagUtils
				.requestContextProviderFrom(pageContext);

		return getFromCurrentPath(request, injectorsRepository, requestContextProvider, type, appName);
	}

	public static <T> T getFromCurrentPath(final SlingHttpServletRequest request,
			final InjectorsRepository injectorsRepository,
			final RequestContextProvider requestContextProvider, final Class<T> type) {
		return getFromCurrentPath(request, injectorsRepository, requestContextProvider, type, null);
	}

	public static <T> T getFromCurrentPath(final SlingHttpServletRequest request,
			final InjectorsRepository injectorsRepository,
			final RequestContextProvider requestContextProvider, final Class<T> type, final String appName) {
		String injectorName = getInjectorName(request, appName);
		String injectorPath = getInjectorPath(request, appName);
		return getFromCurrentPath(request, injectorsRepository,
				requestContextProvider.getContextProvider(injectorName), type, injectorPath);
	}

	public static SlingHttpServletRequest slingRequestFrom(final PageContext pageContext) {
		return (SlingHttpServletRequest) pageContext.getRequest();
	}

	public static ContextProvider contextProviderFrom(final PageContext pageContext) {
		return requestContextProviderFrom(pageContext).getContextProvider(ContextFactory.COMMON_CONTEXT_NAME);
	}

	public static RequestContextProvider requestContextProviderFrom(final PageContext pageContext) {
		final SlingScriptHelper slingScriptHelper = getSlingScriptHelper(pageContext);
		return slingScriptHelper.getService(RequestContextProvider.class);
	}

	public static InjectorsRepository injectorsRepositoryFrom(final PageContext pageContext) {
		final SlingScriptHelper slingScriptHelper = getSlingScriptHelper(pageContext);
		return slingScriptHelper.getService(InjectorsRepository.class);
	}

	private static SlingScriptHelper getSlingScriptHelper(final PageContext pageContext) {
		ServletRequest request = pageContext.getRequest();
		SlingBindings bindings = (SlingBindings) request.getAttribute(SlingBindings.class.getName());
		return bindings.getSling();
	}

	@SuppressWarnings("deprecation")
	private static String getInjectorName(final SlingHttpServletRequest request, final String appName) {
		String injectorName;
		if (StringUtils.isNotBlank(appName)) {
			injectorName = appName;
		} else {
			injectorName = InjectorNameUtil.getFromRequest(request);
		}
		if (StringUtils.isBlank(injectorName)) {
			throw new IllegalStateException("Guice injector name not available");
		}
		return injectorName;
	}

	/**
	 * @deprecated Use
	 * {@code #getFromCurrentPath(SlingHttpServletRequest, InjectorsRepository, RequestContextProvider, Class)}
	 */
	@Deprecated
	public static <T> T getFromCurrentPath(final SlingHttpServletRequest request,
			final InjectorsRepository injectorsRepository, final ContextProvider contextProvider,
			final Class<T> type) {
		return getFromCurrentPath(request, injectorsRepository, contextProvider, type, null);
	}

	/**
	 * @deprecated Use
	 * {@code #getFromCurrentPath(SlingHttpServletRequest, InjectorsRepository, RequestContextProvider, Class, String)}
	 */
	@Deprecated
	public static <T> T getFromCurrentPath(final SlingHttpServletRequest request,
			final InjectorsRepository injectorsRepository, final ContextProvider contextProvider,
			final Class<T> type, final String appName) {

		final String injectorPath = getInjectorPath(request, appName);

		if (null == contextProvider) {
			throw new IllegalStateException("ContextProvider is not available");
		}

		final InjectorWithContext injector = injectorsRepository.getInjectorByPath(injectorPath);
		if (injector == null) {
			throw new IllegalStateException("Guice injector not found: " + injectorPath);
		}

		injector.pushContextProvider(contextProvider);
		try {
			final ModelProvider modelProvider = injector.getInstance(ModelProvider.class);
			final Resource resource = request.getResource();
			return (T) modelProvider.get(type, resource);
		} finally {
			injector.popContextProvider();
		}
	}

	private static String getInjectorPath(SlingHttpServletRequest request, String appName) {

		String injectorPath;
		if (StringUtils.isNotBlank(appName)) {
			injectorPath = appName;
		} else {
			injectorPath = getPathFromRequest(request);
		}
		if (StringUtils.isBlank(injectorPath)) {
			throw new IllegalStateException("Guice injector path not available");
		}
		return injectorPath;
	}

	private static String getPathFromRequest(final SlingHttpServletRequest request)
	{
		if (null != request.getResource()) {
			return request.getResource().getResourceType();
		}
		return StringUtils.EMPTY;
	}
}
