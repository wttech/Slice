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
final class SliceResourceMethodLauncher {

	private static final Logger LOG = LoggerFactory.getLogger(SliceResourceMethodLauncher.class);

	private static final Map<Key, MethodWrapper> ANNOTATED_METHODS_CACHE = new ConcurrentHashMap<Key, MethodWrapper>();

	private SliceResourceMethodLauncher() {
		// utility class
	}

	public static void invokeMethodFor(final Class<? extends Annotation> annotationClass, final Object injectee) {

		Key key = new Key(annotationClass, injectee.getClass());

		if (!ANNOTATED_METHODS_CACHE.containsKey(key)) {
			ANNOTATED_METHODS_CACHE.put(key, findMethod(injectee, annotationClass));
		}
		MethodWrapper method = ANNOTATED_METHODS_CACHE.get(key);

		if (method.isPresent()) {
			invoke(method.get(), injectee);
		}
	}

	private static void invoke(final Method method, final Object injectee) {
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

	private static MethodWrapper findMethod(final Object injectee, final Class<? extends Annotation> annotationClass) {
		for (Method method : injectee.getClass().getDeclaredMethods()) {
			if (method.isAnnotationPresent(annotationClass)) {
				return new MethodWrapper(method);
			}
		}
		return MethodWrapper.EMPTY;
	}

	private static class Key {

		private final Class<? extends Annotation> annotation;

		private final Class<?> injectee;

		public Key(Class<? extends Annotation> annotation, Class<?> injectee) {
			this.annotation = annotation;
			this.injectee = injectee;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			Key key = (Key) o;
			return annotation.equals(key.annotation) && injectee.equals(key.injectee);
		}

		@Override
		public int hashCode() {
			int result = annotation.hashCode();
			return 31 * result + injectee.hashCode();
		}
	}

	private static class MethodWrapper {

		private final Method method;

		private MethodWrapper(Method method) {
			this.method = method;
		}

		private Method get() {
			return method;
		}

		private boolean isPresent() {
			return method != null;
		}

		private static final MethodWrapper EMPTY = new MethodWrapper(null);
	}
}
