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

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import static org.mockito.Mockito.when;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class BundleFinderTest {

	@Mock
	private BundleContext bundleContext;

	@Mock
	private Bundle matchingBundle;

	@Mock
	private Bundle notMatchingBundle;

	private BundleInfo bundleInfo;

	@Before
	public void setUp() {
		bundleInfo = new BundleInfo("matchingFilter", "basePackage");
		when(bundleContext.getBundles()).thenReturn(new Bundle[] { matchingBundle, notMatchingBundle });
		when(matchingBundle.getSymbolicName()).thenReturn("matchingFilter");
		when(notMatchingBundle.getSymbolicName()).thenReturn("notMatchingFilter");
	}

	@Test
	public void testFindBundles() throws Exception {
		BundleFinder bundleFinder = new BundleFinder(bundleInfo, bundleContext);
		List<Bundle> bundles = bundleFinder.findBundles();
		Assert.assertEquals(bundles.size(), 1);
		Assert.assertEquals(bundles.get(0), matchingBundle);

	}
}