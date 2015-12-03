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
import java.util.Map;
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
enum MethodLauncher {
	INSTANCE;

	private static final Logger LOG = LoggerFactory.getLogger(MethodLauncher.class);

	private Map<MethodLauncherCacheKey, MethodWrapper> annotatedMethodsCache = new ConcurrentHashMap<MethodLauncherCacheKey, MethodWrapper>();

	void invokeMethodFor(final Class<? extends Annotation> annotationClass, final Object injectee) {
		Class<?> injecteeClass = injectee.getClass();
		MethodLauncherCacheKey key = new MethodLauncherCacheKey(annotationClass, injecteeClass);

		if (!annotatedMethodsCache.containsKey(key)) {
			annotatedMethodsCache.put(key, findMethod(injecteeClass, annotationClass));
		}
		MethodWrapper method = annotatedMethodsCache.get(key);

		if (method.isPresent()) {
			invoke(method.get(), injectee);
		}
	}

	private void invoke(final Method method, final Object injectee) {
		try {
			method.setAccessible(true);
			method.invoke(injectee, (Object[]) null);
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

	private MethodWrapper findMethod(final Class<?> injecteeClass, final Class<? extends Annotation> annotationClass) {
		for (Method method : injecteeClass.getDeclaredMethods()) {
			if (method.isAnnotationPresent(annotationClass)) {
				return new MethodWrapper(method);
			}
		}
		return MethodWrapper.EMPTY;
	}

}
