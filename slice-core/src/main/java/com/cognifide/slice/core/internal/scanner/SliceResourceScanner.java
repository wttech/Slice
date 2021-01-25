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

package com.cognifide.slice.core.internal.scanner;

import java.util.Collection;

import org.objectweb.asm.ClassReader;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.core.internal.module.AnnotationReader;
import com.cognifide.slice.core.internal.scanner.BundleClassesFinder.ClassFilter;
import com.cognifide.slice.mapper.annotation.SliceResource;

public class SliceResourceScanner {

	private static final Logger LOG = LoggerFactory.getLogger(SliceResourceScanner.class);

	public Collection<Class<?>> findSliceResources(Bundle bundle, String basePackage) {
		BundleClassesFinder classFinder = new BundleClassesFinder(basePackage);
		classFinder.addFilter(new SliceResourceFilter());

		LOG.info("Searching for classes annotated with SliceResource, packages: {}, bundle: {}", basePackage,
				bundle.getSymbolicName());

		Collection<Class<?>> classes = classFinder.getClasses(bundle);

		LOG.info("Found {} Slice Resource classes. Switch to debug logging level to see them all.",
				classes.size());

		return classes;
	}

	final class SliceResourceFilter implements ClassFilter {
		@Override
		public boolean accepts(ClassReader classReader) {
			AnnotationReader annotationReader = new AnnotationReader();
			classReader.accept(annotationReader, ClassReader.SKIP_DEBUG);
			return annotationReader.isAnnotationPresent(SliceResource.class);
		}
	}

}
