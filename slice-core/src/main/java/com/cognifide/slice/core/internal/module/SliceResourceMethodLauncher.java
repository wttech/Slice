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
package com.cognifide.slice.core.internal.module;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.mapper.annotation.PostMapping;
import com.cognifide.slice.mapper.annotation.PreMapping;

/**
 * It is used by {@link SliceResourceModule} to store methods annotated by {@link PreMapping} and {@link PostMapping}
 *
 * @author Krzysztof Watral
 */
final class SliceResourceMethodLauncher {

	private static final Logger LOG = LoggerFactory.getLogger(SliceResourceMethodLauncher.class);

	private static final Map<Class<? extends Annotation>, Map<Class<?>, Method>> ANNOTATED_METHODS_CACHE;
	private static final Map<Class<? extends Annotation>, Set<Class<?>>> MISSING_ANNOTATED_METHODS_CACHE;

	static {
		ANNOTATED_METHODS_CACHE = new HashMap<Class<? extends Annotation>, Map<Class<?>, Method>>();
		ANNOTATED_METHODS_CACHE.put(PreMapping.class, new ConcurrentHashMap<Class<?>, Method>());
		ANNOTATED_METHODS_CACHE.put(PostMapping.class, new ConcurrentHashMap<Class<?>, Method>());

		MISSING_ANNOTATED_METHODS_CACHE = new HashMap<Class<? extends Annotation>, Set<Class<?>>>();
		MISSING_ANNOTATED_METHODS_CACHE.put(PreMapping.class,
				Collections.newSetFromMap(new ConcurrentHashMap<Class<?>, Boolean>()));
		MISSING_ANNOTATED_METHODS_CACHE.put(PostMapping.class,
				Collections.newSetFromMap(new ConcurrentHashMap<Class<?>, Boolean>()));
	}

	private SliceResourceMethodLauncher() {
		// utility class
	}

	public static void invokeMethodFor(final Class<? extends Annotation> annotationClass,
			final Object injectee) {
		final Map<Class<?>, Method> methodCache = ANNOTATED_METHODS_CACHE.get(annotationClass);
		final Set<Class<?>> missingMethodCache = MISSING_ANNOTATED_METHODS_CACHE.get(annotationClass);

		final Class<?> injecteeClass = injectee.getClass();

		Method method = null;
		if (methodCache.containsKey(injecteeClass)) {
			method = methodCache.get(injecteeClass);
		} else if (!missingMethodCache.contains(injecteeClass)) {
			method = findMethod(injectee, annotationClass);
			if (method == null) {
				missingMethodCache.add(injecteeClass);
			} else {
				methodCache.put(injecteeClass, method);
			}
		}

		if (method != null) {
			invoke(method, injectee);
		}
	}

	private static void invoke(final Method method, final Object injectee) {
		try {
			method.setAccessible(true);
			method.invoke(injectee, null);
			LOG.debug("Method " + injectee.getClass().getCanonicalName() + "." + method.getName() +
					"() has been invoked properly.");
		} catch (IllegalAccessException e) {
			LOG.error("Exception while invoking " + injectee.getClass().getCanonicalName() + "." +
					method.getName() + "() : " + e.getMessage(), e);
		} catch (InvocationTargetException e) {
			LOG.error("Exception while invoking " + injectee.getClass().getCanonicalName() + "." +
					method.getName() + "() : " + e.getMessage(), e);
		}
	}

	private static Method findMethod(final Object injectee, final Class<? extends Annotation> annotationClass) {
		for (Method method : injectee.getClass().getDeclaredMethods()) {
			if (method.isAnnotationPresent(annotationClass)) {
				return method;
			}
		}
		return null;
	}
}
