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

package com.cognifide.slice.core.internal.scanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.objectweb.asm.ClassReader;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.cognifide.slice.core.internal.module.AnnotationReader;
import com.cognifide.slice.core.internal.scanner.BundleClassesFinder.ClassFilter;
import com.cognifide.slice.mapper.annotation.SliceResource;


public class SliceResourceScanner {
	private final BundleContext bundleContext;

	public SliceResourceScanner(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public Collection<Class<?>> findSliceResources(String bundleNameFilter, String basePackage) {
		List<Bundle> bundles = findBundles(bundleNameFilter);
		BundleClassesFinder classFinder = new BundleClassesFinder(bundles, basePackage);
		classFinder.addFilter(new ClassFilter() {
			@Override
			public boolean accepts(ClassReader classReader) {
				AnnotationReader annotationReader = new AnnotationReader();
				classReader.accept(annotationReader, ClassReader.SKIP_DEBUG);
				return annotationReader.isAnnotationPresent(SliceResource.class);
			}
		});
		return classFinder.getClasses();
	}

	private List<Bundle> findBundles(String bundleNameFilter) {
		Pattern bundleNamePattern = Pattern.compile(bundleNameFilter);
		List<Bundle> bundles = new ArrayList<Bundle>();
		for (Bundle bundle : bundleContext.getBundles()) {
			if (bundleNamePattern.matcher(bundle.getSymbolicName()).matches()) {
				bundles.add(bundle);
			}
		}
		return bundles;
	}
}
