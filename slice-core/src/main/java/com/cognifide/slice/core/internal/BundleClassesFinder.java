package com.cognifide.slice.core.internal;

/*
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


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BundleClassesFinder {

	private static final Logger LOG = LoggerFactory.getLogger(BundleClassesFinder.class);

	private static final String RESOURCE_PATTERN = "*.class";

	private final Collection<Bundle> bundles;

	private final String basePackage;

	private List<ClassFilter> filters = new ArrayList<BundleClassesFinder.ClassFilter>();

	public BundleClassesFinder(Collection<Bundle> bundles, String basePackage) {
		this.bundles = bundles;
		this.basePackage = basePackage.replace(".", "/");
	}

	public Collection<Class<?>> getClasses() {

		Collection<Class<?>> classes = new ArrayList<Class<?>>();

		for (Bundle bundle : this.bundles) {

			if (LOG.isInfoEnabled()) {
				LOG.info("Searching for classes annotated with SliceResource in '"
						+ bundle.getSymbolicName() + "' bundle, package: " + this.basePackage);
			}

			@SuppressWarnings("unchecked")
			Enumeration<URL> classEntries = bundle.findEntries(this.basePackage, RESOURCE_PATTERN, true);

			while ((classEntries != null) && classEntries.hasMoreElements()) {
				try {
					URL classURL = classEntries.nextElement();
					ClassReader classReader = new ClassReader(classURL.openStream());

					if (accepts(classReader)) {
						String className = classReader.getClassName().replace("/", ".");

						if (LOG.isDebugEnabled()) {
							LOG.debug("Slice Resource class: " + className + " has been found.");
						}

						Class<?> clazz = bundle.loadClass(className);
						classes.add(clazz);
					}
				} catch (ClassNotFoundException e) {
					LOG.error("Error loading class!", e);
				} catch (IOException e) {
					LOG.error("Error reading the class!", e);
				}
			}
		}

		if (LOG.isInfoEnabled()) {
			LOG.info("Found: " + classes.size()
					+ " Slice Resource classes. Switch to debug logging level to see them all.");
		}

		return classes;
	}

	private boolean accepts(ClassReader classReader) {
		for (ClassFilter classFilter : this.filters) {
			if (classFilter.accepts(classReader)) {
				return true;
			}
		}
		return false;
	}

	public void addFilter(ClassFilter classFilter) {
		this.filters.add(classFilter);
	}

	public interface ClassFilter {
		boolean accepts(ClassReader classReader);
	}

}
