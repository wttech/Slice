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

import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.api.model.InitializableModel;
import com.cognifide.slice.core.internal.provider.CurrentResourceProvider;
import com.cognifide.slice.mapper.annotation.PostMapping;
import com.cognifide.slice.mapper.annotation.PreMapping;
import com.cognifide.slice.mapper.annotation.SliceResource;
import com.cognifide.slice.mapper.api.Mapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * This module creates an InjectorListener that postprocesses every injection of a {@code @SliceResource},
 * maps its {@code @JcrProperty} fields and invokes {@code afterCreated()} method.
 * 
 * @author Tomasz Rękawek
 * 
 */
public class SliceResourceModule extends AbstractModule {

	private final SliceResourceAnnotationCache annotationCache = new SliceResourceAnnotationCache();
	private static final Logger LOG = LoggerFactory.getLogger(SliceResourceModule.class);

	@Override
	protected void configure() {
		final Provider<CurrentResourceProvider> currentResourceProvider = getProvider(CurrentResourceProvider.class);
		final Provider<Mapper> mapperProvider = getProvider(Mapper.class);

		// listener that checks if given injectee is annotated with @SliceResource
		AbstractMatcher<TypeLiteral<?>> matcher = new AbstractMatcher<TypeLiteral<?>>() {
			@Override
			public boolean matches(TypeLiteral<?> t) {
				return annotationCache.annotationPresent(t.getRawType());
			}
		};
		// register SliceResourceTypeListener for classes matched with matcher
		TypeListener listener = new TypeListener() {
			@Override
			public <I> void hear(final TypeLiteral<I> type, final TypeEncounter<I> encounter) {
				encounter.register(new SliceResourceTypeListener(currentResourceProvider, mapperProvider));
			}
		};
		bindListener(matcher, listener);
	}

	private static class SliceResourceTypeListener implements InjectionListener<Object> {

		private final Provider<CurrentResourceProvider> currentResourceProvider;

		private final Provider<Mapper> mapperProvider;

		public SliceResourceTypeListener(Provider<CurrentResourceProvider> currentResourceProvider,
				Provider<Mapper> mapperProvider) {
			this.currentResourceProvider = currentResourceProvider;
			this.mapperProvider = mapperProvider;
		}

		@Override
		public void afterInjection(Object injectee) {
			Resource resource = currentResourceProvider.get().get();
			if (resource == null) {
				return;
			}

			Mapper mapper = mapperProvider.get();

			SliceResourceMethodLauncher.invokeMethodFor(PreMapping.class, injectee);
			mapper.get(resource, injectee);
			SliceResourceMethodLauncher.invokeMethodFor(PostMapping.class, injectee);

			if (injectee instanceof InitializableModel) {
				((InitializableModel) injectee).afterCreated();
			}
		}
	}

	private static class SliceResourceAnnotationCache {
		private Map<Class<?>, Boolean> cache = new HashMap<Class<?>, Boolean>();

		public boolean annotationPresent(Class<?> clazz) {
			if (!cache.containsKey(clazz)) {
				cache.put(clazz, clazz.isAnnotationPresent(SliceResource.class));
			}
			return cache.get(clazz);
		}
	}

	private static class SliceResourceMethodLauncher {
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
}
