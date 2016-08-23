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

package com.cognifide.slice.api.tag;

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
import org.apache.sling.commons.classloader.DynamicClassLoaderManager;

public final class SliceTagUtils {

	private SliceTagUtils() {
		// hidden constructor
	}

	/**
	 * A helper method that returns a model of a page
	 *
	 * @param pageContext allows to access resource path, request context and injector instance
	 * @param type of model that should be created
	 * @param <T> type of object to be returned
	 * @return a model of type T of the page indicated by pageContext
	 */
	public static <T> T getFromCurrentPath(final PageContext pageContext, final Class<T> type) {
		return getFromCurrentPath(pageContext, type, null);
	}

	/**
	 * A helper method that returns a model of a page
	 *
	 * @param pageContext allows to access resource path, request context and injector instance
	 * @param type of model that should be created
	 * @param appName - name of application needed to find a proper injector in multi-app environment
	 * @param <T> type of object to be returned
	 * @return a model of type T of the page indicated by pageContext
	 */
	public static <T> T getFromCurrentPath(final PageContext pageContext, final Class<T> type,
			final String appName) {
		final SlingHttpServletRequest request = SliceTagUtils.slingRequestFrom(pageContext);
		final InjectorsRepository injectorsRepository = SliceTagUtils.injectorsRepositoryFrom(pageContext);
		final RequestContextProvider requestContextProvider = SliceTagUtils
				.requestContextProviderFrom(pageContext);

		return getFromCurrentPath(request, injectorsRepository, requestContextProvider, type, appName);
	}

	/**
	 * A helper method that returns the {@link Class} object, resolving it via it's Canonical Name.
	 *
	 * @param pageContext allows to access request context
	 * @param type canonical name of the modal object, whose {@link Class} object needs to be returned
	 * @return {@link Class} object pertaining to {@code type} as it's canonical name
	 * @throws ClassNotFoundException if the class was not found
	 */
	public static Class<?> getClassFromType(final PageContext pageContext, final String type) throws ClassNotFoundException {
		final SlingScriptHelper scriptHelper = getSlingScriptHelper(pageContext);
		final DynamicClassLoaderManager dynamicClassLoaderManager = scriptHelper
				.getService(DynamicClassLoaderManager.class);
		final ClassLoader classLoader = dynamicClassLoaderManager.getDynamicClassLoader();
		return classLoader.loadClass(type);
	}

	/**
	 * A helper method that returns a model of the Sling resource related to given request
	 *
	 * @param request request
	 * @param injectorsRepository injectors repository
	 * @param requestContextProvider conetxt provider
	 * @param type of model that should be created
	 * @param <T> type of object to be returned
	 * @return a model of type T
	 */
	public static <T> T getFromCurrentPath(final SlingHttpServletRequest request,
			final InjectorsRepository injectorsRepository,
			final RequestContextProvider requestContextProvider, final Class<T> type) {
		return getFromCurrentPath(request, injectorsRepository, requestContextProvider, type, null);
	}

	public static <T> T getFromCurrentPath(final SlingHttpServletRequest request,
			final InjectorsRepository injectorsRepository,
			final RequestContextProvider requestContextProvider, final Class<T> type, final String appName) {
		final String injectorName = getInjectorName(request, appName, injectorsRepository);
		return getFromCurrentPath(request, injectorsRepository,
				requestContextProvider.getContextProvider(injectorName), type, injectorName);
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

	private static String getInjectorName(final SlingHttpServletRequest request, final String appName,
			final InjectorsRepository injectorsRepository) {
		String injectorName;
		if (StringUtils.isNotBlank(appName)) {
			injectorName = appName;
		} else {
			injectorName = getFromRequest(request, injectorsRepository);
		}
		if (StringUtils.isBlank(injectorName)) {
			throw new IllegalStateException("Guice injector name not available");
		}
		return injectorName;
	}

	private static String getFromRequest(final SlingHttpServletRequest request,
			InjectorsRepository injectorsRepository) {
		if (request.getResource() != null) {
			String resourceType = request.getResource().getResourceType();
			return injectorsRepository.getInjectorNameForResource(resourceType);
		}
		return StringUtils.EMPTY;
	}

	/**
	 * @param request request
	 * @param injectorsRepository injectors repository
	 * @param type of model that should be created
	 * @param <T> type of object to be returned
	 * @param contextProvider context provider
	 * @return a model of type T
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
	 * @param request request
	 * @param injectorsRepository injectors repository
	 * @param type of model that should be created
	 * @param <T> type of object to be returned
	 * @param contextProvider context provider
	 * @param appName application name
	 * @return a model of type T
	 * 
	 * @deprecated Use
	 * {@code #getFromCurrentPath(SlingHttpServletRequest, InjectorsRepository, RequestContextProvider, Class, String)}
	 */
	@Deprecated
	public static <T> T getFromCurrentPath(final SlingHttpServletRequest request,
			final InjectorsRepository injectorsRepository, final ContextProvider contextProvider,
			final Class<T> type, final String appName) {
		final String injectorName = getInjectorName(request, appName, injectorsRepository);

		if (null == contextProvider) {
			throw new IllegalStateException("ContextProvider is not available");
		}

		final InjectorWithContext injector = injectorsRepository.getInjector(injectorName);
		if (injector == null) {
			throw new IllegalStateException("Guice injector not found: " + injectorName);
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
}
