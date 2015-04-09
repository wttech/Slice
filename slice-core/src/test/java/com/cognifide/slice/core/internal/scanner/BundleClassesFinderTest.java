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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class BundleClassesFinderTest {

	private BundleClassesFinder classFinder;

	@Before
	public void setUp() {
		classFinder = new BundleClassesFinder("test");
	}

	@Test
	public void testTraverseBundlesForOsgiServicesInnerClass() {
		Collection<Class<?>> classes = classFinder
				.readOsgiServicesForClass(InnerClassTestService.InnerClass.class);
		Assert.assertEquals(1, classes.size());
		Assert.assertTrue(classes.contains(Integer.class));
	}

	@Test
	public void testTraverseBundlesForOsgiServicesWithInnerClass() {
		Collection<Class<?>> classes = classFinder.readOsgiServicesForClass(InnerClassTestService.class);
		Assert.assertEquals(0, classes.size());
	}

	@Test
	public void testTraverseBundlesForOsgiServices() {
		Collection<Class<?>> classes = classFinder.readOsgiServicesForClass(ClassTestService.class);
		Assert.assertEquals(2, classes.size());
		Assert.assertTrue(classes.contains(String.class));
		Assert.assertTrue(classes.contains(Long.class));
	}
	
	@Test
	public void testTraverseBundlesForOsgiWithConstructorServices() {
		Collection<Class<?>> classes = classFinder.readOsgiServicesForClass(ClassTestConstructorService.class);
		Assert.assertEquals(2, classes.size());
		Assert.assertTrue(classes.contains(Double.class));
		Assert.assertTrue(classes.contains(Long.class));
	}
}