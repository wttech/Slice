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

package com.cognifide.slice.core.internal.provider;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.api.context.ContextProvider;
import com.cognifide.slice.api.context.ContextScope;
import com.cognifide.slice.api.execution.ExecutionContextStack;
import com.cognifide.slice.api.provider.ModelProvider;
import com.cognifide.slice.api.scope.ContextScoped;
import com.cognifide.slice.core.internal.execution.ExecutionContextImpl;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;

import javax.servlet.http.Cookie;
import javax.ws.rs.*;

/**
 * This class creates object or list of objects of given injectable type using Guice injector.
 * 
 * @author Witold Szczerba
 * @author Rafał Malinowski
 */
@ContextScoped
public class SliceModelProvider implements ModelProvider {

	private static final Logger LOG = LoggerFactory.getLogger(SliceModelProvider.class);

	private final Injector injector;

	private final ContextScope contextScope;

	private final ContextProvider contextProvider;

	private final ClassToKeyMapper classToKeyMapper;

	private final ExecutionContextStack currentExecutionContext;

	private final ResourceResolver resourceResolver;

	/**
	 * {@inheritDoc}
	 */
	@Inject
	public SliceModelProvider(final Injector injector, final ContextScope contextScope,
			final ClassToKeyMapper classToKeyMapper, final ExecutionContextStack currentExecutionContext,
			final ResourceResolver resourceResolver) {
		this.injector = injector;
		this.contextScope = contextScope;
		this.contextProvider = contextScope.getContextProvider();
		this.classToKeyMapper = classToKeyMapper;
		this.currentExecutionContext = currentExecutionContext;
		this.resourceResolver = resourceResolver;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <T> T get(final Class<T> type, final String path) {
		/*
		 * This method is not synchronized on purpose, the only case where concurrency issues may occur is
		 * when the contentPathContext variable is used from different threads, and since the
		 * CurrentPathContext is request scoped it would mean multiple threads in same request which then is
		 * against servlet specification.
		 */
		ExecutionContextImpl executionItem = new ExecutionContextImpl(path);
		LOG.debug("creating new instance of {} from {}", new Object[] { type.getName(), path });
		return get(type, executionItem);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T get(Class<T> type, Resource resource) {
		ExecutionContextImpl executionItem = new ExecutionContextImpl(resource);
		LOG.debug("creating new instance of {} from {}", new Object[] { type.getName(), resource });
		return get(type, executionItem);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T get(Key<T> key, Resource resource) {
		ExecutionContextImpl executionItem = new ExecutionContextImpl(resource);
		LOG.debug("creating new instance of {} from {}", new Object[] { key.toString(), resource });
		return get(key, executionItem);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T get(Key<T> key, String path) {
		ExecutionContextImpl executionItem = new ExecutionContextImpl(path);
		LOG.debug("creating new instance of {} from {}", new Object[] { key.toString(), path });
		return get(key, executionItem);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object get(String className, String path) throws ClassNotFoundException {
		final Key<?> key = classToKeyMapper.getKey(className);
		if (key == null) {
			throw new ClassNotFoundException("key for class " + className + " not found");
		}
		ExecutionContextImpl executionItem = new ExecutionContextImpl(path);
		LOG.debug("creating new instance for {} from {}", new Object[] { key.toString(), path });
		return get(key, executionItem);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object get(String className, Resource resource) throws ClassNotFoundException {
		final Key<?> key = classToKeyMapper.getKey(className);
		if (key == null) {
			throw new ClassNotFoundException("key for class " + className + " not found");
		}
		ExecutionContextImpl executionItem = new ExecutionContextImpl(resource);
		LOG.debug("creating new instance for {} from {}", new Object[] { key.toString(), resource });
		return get(key, executionItem);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <T> List<T> getList(final Class<T> type, final Iterator<String> paths) {
		final ArrayList<T> result = new ArrayList<T>();

		if (paths == null) {
			return result;
		}

		while (paths.hasNext()) {
			final String path = paths.next();
			final T model = get(type, path);
			result.add(model);
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <T> List<T> getList(final Class<T> type, final String[] paths) {
		if (paths == null) {
			return new ArrayList<T>();
		}
		return getList(type, Arrays.asList(paths).iterator());
	}

	@Override
	public <T> List<T> getListFromResources(Class<T> type, Iterator<Resource> resources) {
		List<T> result = new ArrayList<T>();
		while (resources != null && resources.hasNext()) {
			Resource resource = resources.next();
			T model = get(type, resource);
			result.add(model);
		}
		return result;
	}

	@Override
	public <T> T get(Class<T> clazz, SlingHttpServletRequest request) throws ClassNotFoundException {
		T instance = injector.getInstance(clazz);

		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(FormParam.class)) {
				String parameterName = field.getAnnotation(FormParam.class).value();
				String stringValue = request.getRequestParameter(parameterName).getString();
				instance = insertValue(instance, field, stringValue);
			} else if (field.isAnnotationPresent(CookieParam.class)) {
				String parameterName = field.getAnnotation(CookieParam.class).value();
				Cookie cookie = request.getCookie(parameterName);
				if (cookie != null) {
					field.setAccessible(true);
					try {
						field.set(instance, cookie);
					} catch (IllegalAccessException e) {
						LOG.error("Cannot insert cookie {} into field {}", cookie.getName(), field.getName());
					}
				}
			} else if (field.isAnnotationPresent(HeaderParam.class)) {
				String parameterName = field.getAnnotation(HeaderParam.class).value();
				String stringValue = request.getHeader(parameterName);
				instance = insertValue(instance, field, stringValue);
			}
		}
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> List<T> getChildModels(Class<T> type, String parentPath) {
		String absolutePath = currentExecutionContext.getAbsolutePath(parentPath);
		Resource resource = resourceResolver.getResource(absolutePath);
		return getChildModels(type, resource);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> List<T> getChildModels(Class<T> type, Resource parentResource) {
		final List<T> result = new ArrayList<T>();
		if (parentResource != null) {
			Iterator<Resource> listChildren = parentResource.listChildren();
			while (listChildren.hasNext()) {
				Resource childResource = listChildren.next();
				T childModel = get(type, childResource);
				result.add(childModel);
			}
		}
		return result;
	}

	private <T> T insertValue(T instance, Field field, String stringValue) {
		Object parameterValue = convert(field.getType(), stringValue);
		if (parameterValue != null) {
			try {
				field.setAccessible(true);
				field.set(instance, parameterValue);
			} catch (IllegalAccessException e) {
				LOG.error("Cannot insert value {} into field {}", stringValue, field.getName());
			}
		}
		return instance;
	}

	private Object convert(Class<?> targetType, String text) {
		PropertyEditor editor = PropertyEditorManager.findEditor(targetType);
		if (text.isEmpty()) {
			text = null;
		}
		editor.setAsText(text);
		return editor.getValue();
	}

	private <T> T get(Class<T> type, ExecutionContextImpl executionItem) {
		return get(Key.get(type), executionItem);
	}

	private <T> T get(Key<T> key, ExecutionContextImpl executionItem) {
		final ContextProvider oldContextProvider = contextScope.getContextProvider();
		contextScope.setContextProvider(contextProvider);

		if ((executionItem.getResource() == null) && (executionItem.getPath() != null)) {
			executionItem.setPath(currentExecutionContext.getAbsolutePath(executionItem.getPath()));
		}

		currentExecutionContext.push(executionItem);
		try {
			return injector.getInstance(key);
		} finally {
			currentExecutionContext.pop();
			contextScope.setContextProvider(oldContextProvider);
		}
	}
}
