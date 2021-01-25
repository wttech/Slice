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
package com.cognifide.slice.core.internal.module;

import java.util.Collection;

import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.builders.DecoratedServiceBuilder;
import org.osgi.framework.BundleContext;

import com.cognifide.slice.api.scope.ContextScoped;
import com.cognifide.slice.core.internal.scanner.OsgiServiceScanner;
import com.google.inject.AbstractModule;
import com.google.inject.binder.AnnotatedBindingBuilder;

/**
 * @author Jaromir Celejewski
 * This module class will scan given bundles and automatically bind all OSGi services marked with @OsgiService
 * annotation using Peaberry. Instance of this module should be installed in injector:
 * injectorRunner.installModule(new OsgiToGuiceAutoBindModule(bundleContext, bundle_name_filter,
 * base_package)); Also @see com.cognifide.slice.annotations.OsgiService
 */
public class OsgiToGuiceAutoBindModule extends AbstractModule {

	private final String bundleNameFilter;

	private final String basePackage;

	private final BundleContext bundleContext;

	public OsgiToGuiceAutoBindModule(BundleContext bundleContext, String bundleNameFilter,
			String basePackage) {
		this.bundleNameFilter = bundleNameFilter;
		this.basePackage = basePackage;
		this.bundleContext = bundleContext;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configure() {
		OsgiServiceScanner scanner = new OsgiServiceScanner(bundleContext);
		Collection<Class<?>> requestedOsgiClasses = scanner.findResources(bundleNameFilter, basePackage);
		for (Class<?> clazz : requestedOsgiClasses) {
			((AnnotatedBindingBuilder<Object>) bind(clazz))
					.toProvider(((DecoratedServiceBuilder<Object>) Peaberry.service(clazz)).single())
					.in(ContextScoped.class);
		}
	}
}
