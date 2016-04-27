/*-
 * #%L
 * Slice - Persistence
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
package com.cognifide.slice.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.tools.ant.filters.StringInputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.cognifide.slice.mapper.annotation.JcrProperty;
import com.cognifide.slice.mapper.annotation.SliceResource;

public class NativeCollectionTest extends BaseTest {

	private static final Calendar CAL1 = Calendar.getInstance();

	private static final Calendar CAL2 = Calendar.getInstance();

	@SliceResource
	private static class TestClass {

		@JcrProperty
		private List<Long> longList = Arrays.asList(1l, 2l, 3l);

		@JcrProperty
		private List<Integer> intList = Arrays.asList(1, 2, 3);

		@JcrProperty
		private List<Boolean> boolList = Arrays.asList(true, false, true);

		@JcrProperty
		private List<InputStream> binaryList = Arrays.asList((InputStream) new StringInputStream("abc"),
				new StringInputStream("123"), new StringInputStream("abc"));

		@JcrProperty
		private List<String> stringList = Arrays.asList("abc", "123", "abc");

		@JcrProperty
		private List<Calendar> calendarList = Arrays.asList(CAL1, CAL2);

		@JcrProperty
		private List<Double> doubleList = Arrays.asList(1.0, 2.0, 3.0);
	}

	private ValueMap map;

	@Before
	public void beforeTest() throws LoginException, PersistenceException, IllegalAccessException {
		modelPersister.persist(new TestClass(), "nativeCollectionTest", resolver.getResource("/"));
		resolver.commit();
		map = resolver.getResource("/nativeCollectionTest").adaptTo(ValueMap.class);
	}

	@Test
	public void longCollectionTest() {
		Assert.assertArrayEquals(new Long[] { 1l, 2l, 3l }, (Long[]) map.get("longList"));
	}

	@Test
	public void intCollectionTest() {
		Assert.assertArrayEquals(new Long[] { 1l, 2l, 3l }, (Long[]) map.get("intList"));
	}

	@Test
	public void boolCollectionTest() {
		Assert.assertArrayEquals(new Boolean[] { true, false, true }, (Boolean[]) map.get("boolList"));
	}

	@Test
	public void binaryCollectionTest() throws IOException {
		final List<String> result = new ArrayList<String>();
		for (final Object is : (Object[]) map.get("binaryList")) {
			result.add(IOUtils.toString((InputStream) is));
		}
		Assert.assertEquals(Arrays.asList("abc", "123", "abc"), result);
	}

	@Test
	public void stringCollectionTest() {
		Assert.assertArrayEquals(new String[] { "abc", "123", "abc" }, (String[]) map.get("stringList"));
	}

	@Test
	public void calendarCollectionTest() {
		Assert.assertArrayEquals(new Calendar[] { CAL1, CAL2 }, (Calendar[]) map.get("calendarList"));
	}

	@Test
	public void doubleCollectionTest() {
		Assert.assertArrayEquals(new Double[] { 1.0, 2.0, 3.0 }, (Double[]) map.get("doubleList"));
	}

	@Test
	public void doubleCollectionOverwriteTest() throws PersistenceException {
		TestClass anotherTestClass = new TestClass();
		anotherTestClass.doubleList = Collections.singletonList(3.14159);

		modelPersister.persist(anotherTestClass, "nativeCollectionTest", resolver.getResource("/"));
		resolver.commit();
		Resource overwrittenResource = resolver.getResource("/nativeCollectionTest");
		ValueMap overwrittenMap = overwrittenResource.adaptTo(ValueMap.class);

		Assert.assertArrayEquals(new Double[] { 3.14159 }, (Double[]) overwrittenMap.get("doubleList"));
	}

	@Test
	public void doubleCollectionOverwriteByNullTest() throws PersistenceException {
		TestClass anotherTestClass = new TestClass();
		anotherTestClass.doubleList = null;

		modelPersister.persist(anotherTestClass, "nativeCollectionTest", resolver.getResource("/"));
		resolver.commit();
		Resource overwrittenResource = resolver.getResource("/nativeCollectionTest");
		ValueMap overwrittenMap = overwrittenResource.adaptTo(ValueMap.class);

		Assert.assertFalse(overwrittenMap.containsKey("doubleList"));
	}
}