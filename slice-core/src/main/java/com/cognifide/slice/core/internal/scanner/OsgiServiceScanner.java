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
package com.cognifide.slice.core.internal.scanner;

import java.util.Collection;

import org.objectweb.asm.ClassReader;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jaromir Celejewski Helper class for scanning bundles
 */
public class OsgiServiceScanner {

	private static final Logger LOG = LoggerFactory.getLogger(SliceResourceScanner.class);

	private final BundleContext bundleContext;

	public OsgiServiceScanner(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	/**
	 * Returns collection of all classes that match given bundle filter and package.
	 */
	public Collection<Class<?>> findResources(String bundleNameFilter, String basePackage) {
		BundleClassesFinder classFinder = new BundleClassesFinder(basePackage, bundleNameFilter,
				bundleContext);
		classFinder.addFilter(new BundleClassesFinder.ClassFilter() {
			@Override
			public boolean accepts(ClassReader classReader) {
				// accept all classes
				return true;
			}
		});
		LOG.info("Searching for @OsgiServices, packages:{}, bundles:{}" + basePackage, bundleNameFilter);

		Collection<Class<?>> classes = classFinder.traverseBundlesForOsgiServices();

		LOG.info("Found {} OsgiService classes. Switch to debug logging level to see them all.",
				classes.size());
		return classes;
	}
}
