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

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BundleFinder {

	private static final Logger LOG = LoggerFactory.getLogger(BundleFinder.class);

	private final BundleMatcher matcher;

	private final BundleContext bundleContext;

	public BundleFinder(BundleInfo bundleInfo, BundleContext bundleContext) {
		this.bundleContext = bundleContext;
		this.matcher = new BundleMatcher(bundleInfo);
	}

	public List<Bundle> findBundles() {
		List<Bundle> bundles = new ArrayList<Bundle>();
		for (Bundle bundle : bundleContext.getBundles()) {
			String symbolicName = bundle.getSymbolicName();
			if (matcher.matches(symbolicName)) {
				LOG.debug("Bundle {} has been found.", symbolicName);
				bundles.add(bundle);
			}
		}
		return bundles;
	}
}
