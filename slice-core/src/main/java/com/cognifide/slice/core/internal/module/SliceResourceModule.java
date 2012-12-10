package com.cognifide.slice.core.internal.module;

/*-
 * #%L
 * Slice - Core
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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.objectweb.asm.ClassReader;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.api.qualifier.EmptyObject;
import com.cognifide.slice.commons.provider.SliceResourceProvider;
import com.cognifide.slice.core.internal.BundleClassesFinder;
import com.cognifide.slice.core.internal.BundleClassesFinder.ClassFilter;
import com.cognifide.slice.mapper.annotation.SliceResource;
import com.cognifide.slice.mapper.api.Mapper;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;

/**
 * The module is responsible for finding all classes annotated by {@link SliceResource}. Found classes are
 * bound to their {@link SliceResourceProvider} providers
 * 
 */
public class SliceResourceModule extends AbstractModule {

	private static final Logger LOG = LoggerFactory.getLogger(SliceResourceModule.class);

	private final Collection<Class<?>> classes;

	public SliceResourceModule(final Collection<Class<?>> classes) {
		this.classes = classes;
	}

	public SliceResourceModule(final BundleContext bundleContext, final String bundleNameFilter,
			final String basePackage) {

		final Pattern bundleNamePattern = Pattern.compile(bundleNameFilter);
		List<Bundle> filteredBundles = new ArrayList<Bundle>();

		for (Bundle bundle : bundleContext.getBundles()) {
			if (bundleNamePattern.matcher(bundle.getSymbolicName()).matches()) {
				filteredBundles.add(bundle);
			}
		}

		BundleClassesFinder classFinder = new BundleClassesFinder(filteredBundles, basePackage);
		classFinder.addFilter(new AnnotationFilter());

		this.classes = classFinder.getClasses();
	}

	@Override
	protected void configure() {
		for (Class<?> clazz : this.classes) {
			try {
				bindToOwnProvider(clazz);
			} catch (Exception e) {
				LOG.error("Cannot register " + clazz + " class. Nested exception:", e);
			}
		}
	}

	/**
	 * It binds a given class to appropriate providers. Firstly, the class is bound with {@link EmptyObject}
	 * annotations what allows for creating empty objects (not mapped by {@link Mapper}), and then it is bound
	 * to actual provider which uses mapper to fill the empty object with CRX values.
	 * 
	 * @param <T>
	 * @param sliceResourceClass
	 */
	private <T> void bindToOwnProvider(final Class<T> sliceResourceClass) {
		bindEmptyObject(sliceResourceClass);
		bindActualObject(sliceResourceClass);
	}

	/**
	 * This is an essential method which binds provided class with {@link EmptyObject} annotation. It allows
	 * then to create an empty object of this class with all constructor parameters injected by Guice. Such an
	 * empty object can be further processed by {@link Mapper} in order to fill appropriate fields.<br>
	 * <br>
	 * What the method actually does is to bind the provided class to either constructor annotated with
	 * {@link Inject} annotation or the default (zero-argument) constructor. If none of these constructors are
	 * declared by the class, the {@link IllegalArgumentException} is thrown.
	 * 
	 * @param <T>
	 * @param sliceResourceClass
	 */
	private <T> void bindEmptyObject(final Class<T> sliceResourceClass) {
		Constructor<T> constructor = getInjectConstructor(sliceResourceClass);
		if (constructor == null) {
			throw new IllegalArgumentException(
					"Class must have either one (and only one) constructor annotated with @Inject or a zero-argument constructor that is not private");
		}
		bind(sliceResourceClass).annotatedWith(EmptyObject.class).toConstructor(constructor);
	}

	/**
	 * It binds provided class to appropriate {@link SliceResourceProvider}. When a client requests to create
	 * object of given class, bound provider will be used.
	 * 
	 * @param <T>
	 * @param sliceResourceClass
	 */
	private <T> void bindActualObject(final Class<T> sliceResourceClass) {
		LOG.error("Binding " + sliceResourceClass + " to MRP");
		bind(sliceResourceClass).toProvider(new MappedResourceProvider<T>(sliceResourceClass));
	}

	@SuppressWarnings("unchecked")
	private <T> Constructor<T> getInjectConstructor(final Class<T> sliceResourceClass) {
		final Constructor<?>[] constructors = sliceResourceClass.getConstructors();
		for (final Constructor<?> c : constructors) {
			if (c.isAnnotationPresent(Inject.class) || c.isAnnotationPresent(javax.inject.Inject.class)) {
				return (Constructor<T>) c;
			}
		}

		return getDefaultConstructor(sliceResourceClass);
	}

	private <T> Constructor<T> getDefaultConstructor(final Class<T> sliceResourceClass) {
		try {
			return sliceResourceClass.getConstructor(new Class<?>[0]);
		} catch (SecurityException e) {
			LOG.error("Error while reading empty constructor", e);
		} catch (NoSuchMethodException e) {
			LOG.error("Error while reading empty constructor", e);
		}
		return null;
	}

	private static class AnnotationFilter implements ClassFilter {
		@Override
		public boolean accepts(ClassReader classReader) {
			final AnnotationReader annotationReader = new AnnotationReader();
			classReader.accept(annotationReader, ClassReader.SKIP_DEBUG);
			return annotationReader.isAnnotationPresent(SliceResource.class);
		}
	}

}