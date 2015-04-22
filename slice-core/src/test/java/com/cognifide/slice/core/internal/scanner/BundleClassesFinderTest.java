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

import java.net.URL;
import java.util.*;

import com.cognifide.slice.testhelper.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import junit.framework.Assert;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.objectweb.asm.ClassReader;
import org.osgi.framework.Bundle;

@RunWith(MockitoJUnitRunner.class)
public class BundleClassesFinderTest {

	@Mock
	private Bundle bundle;

	private BundleClassesFinder classFinder;

	@Before
	public void setUp() {
		classFinder = new BundleClassesFinder("test");
	}

	@Test
	public void testReadOsgiServicesForClassInnerClass() {
		Collection<Class<?>> classes = classFinder
				.readOsgiServicesForClass(InnerClassTestService.InnerClass.class);
		Assert.assertEquals(1, classes.size());
		Assert.assertTrue(classes.contains(Integer.class));
	}

	@Test
	public void testReadOsgiServicesForClassWithInnerClass() {
		Collection<Class<?>> classes = classFinder.readOsgiServicesForClass(InnerClassTestService.class);
		Assert.assertEquals(0, classes.size());
	}

	@Test
	public void testReadOsgiServicesForClass() {
		Collection<Class<?>> classes = classFinder.readOsgiServicesForClass(ClassTestService.class);
		Assert.assertEquals(2, classes.size());
		Assert.assertTrue(classes.contains(String.class));
		Assert.assertTrue(classes.contains(Long.class));
	}

	@Test
	public void testReadOsgiServicesForClassWithConstructorServices() {
		Collection<Class<?>> classes = classFinder
				.readOsgiServicesForClass(ClassTestConstructorService.class);
		Assert.assertEquals(2, classes.size());
		Assert.assertTrue(classes.contains(Double.class));
		Assert.assertTrue(classes.contains(Long.class));
	}

	private void bundleSetup() throws ClassNotFoundException {
		final List<String> classList = Lists.newArrayList("com/cognifide/slice/testhelper/TestBundleClass1",
				"com/cognifide/slice/testhelper/TestBundleClass2",
				"com/cognifide/slice/testhelper/TestBundleClass3",
				"com/cognifide/slice/testhelper/TestBundleClass4");

		ClassURLProducer classURLProducer = new ClassURLProducer("slice-core", Sets.newHashSet(classList));
		Set<URL> set = classURLProducer.getUrls();
		Enumeration<URL> classEntries = new Vector(set).elements();
		when(bundle.findEntries("test", "*.class", true)).thenReturn(classEntries);
		when(bundle.loadClass(anyString())).thenAnswer(new Answer<Class>() {
			@Override
			public Class answer(InvocationOnMock invocationOnMock) throws Throwable {
				Object[] args = invocationOnMock.getArguments();
				return Class.forName((String) args[0]);
			}
		});

		classFinder.addFilter(new BundleClassesFinder.ClassFilter() {
			@Override
			public boolean accepts(ClassReader classReader) {
				return classReader.getClassName().equals(classList.get(0))
						|| classReader.getClassName().equals(classList.get(1));
			}
		});

		classFinder.addFilter(new BundleClassesFinder.ClassFilter() {
			@Override
			public boolean accepts(ClassReader classReader) {
				return classReader.getClassName().equals(classList.get(1))
						|| classReader.getClassName().equals(classList.get(2));
			}
		});
	}

	@Test
	public void testGetClasses() throws ClassNotFoundException {
		bundleSetup();
		Collection<Class<?>> classes = classFinder.getClasses(Lists.newArrayList(bundle));
		Assert.assertEquals(classes.size(), 3);
		for (Class clazz : classes) {
			Assert.assertFalse(clazz.getSimpleName().equals("TestBundleClass4"));
		}
	}

	@Test
	public void testTraverseBundlesForOsgiServices() throws ClassNotFoundException {
		bundleSetup();
		Collection<Class<?>> classes = classFinder.traverseBundlesForOsgiServices(Lists.newArrayList(bundle));
		Assert.assertEquals(classes.size(), 1);
		Assert.assertTrue(new ArrayList<Class<?>>(classes).get(0).getSimpleName().equals("Long"));
	}
}