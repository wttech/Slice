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

import java.net.URL;
import java.util.*;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SliceResourceScannerTest {

	@Mock
	private Bundle bundle;

	@Before
	public void setUp() throws ClassNotFoundException {
		final List<String> classList = Lists.newArrayList("com/cognifide/slice/testhelper/TestBundleClass1",
				"com/cognifide/slice/testhelper/TestBundleClass2");

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
	}

	@Test
	public void testFindSliceResources() {
		SliceResourceScanner sliceResourceScanner = new SliceResourceScanner();
		Collection<Class<?>> classes = sliceResourceScanner.findSliceResources(bundle, "test");
		Assert.assertEquals(classes.size(), 1);
		Assert.assertEquals("TestBundleClass2", new ArrayList<Class<?>>(classes).get(0).getSimpleName());
	}
}