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

import com.cognifide.slice.testhelper.ClassURLProducer;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.net.URL;
import java.util.*;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OsgiServiceScannerTest {

	@Mock
	private BundleContext bundleContext;

	@Mock
	private Bundle bundle;

	private OsgiServiceScanner osgiServiceScanner;

	@Before
	public void setUp() throws ClassNotFoundException {
		final List<String> classList = Lists.newArrayList("com/cognifide/slice/testhelper/TestBundleClass1",
				"com/cognifide/slice/testhelper/TestBundleClass2",
				"com/cognifide/slice/testhelper/TestBundleClass3",
				"com/cognifide/slice/testhelper/TestBundleClass4");

		ClassURLProducer classURLProducer = new ClassURLProducer(Sets.newHashSet(classList));
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
		when(bundleContext.getBundles()).thenReturn(new Bundle[] { bundle });
		when(bundle.getSymbolicName()).thenReturn("filterName");
		osgiServiceScanner = new OsgiServiceScanner(bundleContext);
	}

	@Test
	public void testFindResources() {
		Collection<Class<?>> classes = osgiServiceScanner.findResources("filterName", "test");
		Assert.assertEquals(1, classes.size());
		Assert.assertEquals("Long", new ArrayList<Class<?>>(classes).get(0).getSimpleName());

	}
}