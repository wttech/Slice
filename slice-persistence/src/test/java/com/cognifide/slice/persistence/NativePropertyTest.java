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
import java.util.Calendar;

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

public class NativePropertyTest extends BaseTest {

	private static final Calendar CAL = Calendar.getInstance();

	@SliceResource
	private static class TestClass {

		@JcrProperty
		private int intPrimitive = 1;

		@JcrProperty
		private Integer intObject = 1;

		@JcrProperty
		private long longPrimitive = 2l;

		@JcrProperty
		private Long longObject = 2l;

		@JcrProperty
		private double doublePrimitive = 3.14;

		@JcrProperty
		private Double doubleObject = 3.14;

		@JcrProperty
		private boolean booleanPrimitive = true;

		@JcrProperty
		private Boolean booleanObject = true;

		@JcrProperty
		private Calendar calProp = CAL;

		@JcrProperty
		private InputStream binary = new StringInputStream("asd");

		@JcrProperty
		private Object nullable = null;
	}

	@SliceResource
	private static class AnotherTestClass {

		@JcrProperty
		private String prop = "asd";

	}

	private ValueMap map;

	@Before
	public void beforeTest() throws LoginException, PersistenceException, IllegalAccessException {
		modelPersister.persist(new TestClass(), "nativePropertyTest", resolver.getResource("/"));
		resolver.commit();
		map = resolver.getResource("/nativePropertyTest").adaptTo(ValueMap.class);
	}

	@Test
	public void intTest() {
		Assert.assertEquals(1l, map.get("intPrimitive")); // integer are transformed into longs
		Assert.assertEquals(1l, map.get("intObject")); // integer are transformed into longs
	}

	@Test
	public void longTest() {
		Assert.assertEquals(2l, map.get("longPrimitive"));
		Assert.assertEquals(2l, map.get("longObject"));
	}

	@Test
	public void booleanTest() {
		Assert.assertEquals(true, map.get("booleanPrimitive"));
		Assert.assertEquals(true, map.get("booleanObject"));
	}

	@Test
	public void doubleTest() {
		Assert.assertEquals(3.14, map.get("doublePrimitive"));
		Assert.assertEquals(3.14, map.get("doubleObject"));
	}

	@Test
	public void calendarTest() {
		Assert.assertEquals(CAL, map.get("calProp"));
	}

	@Test
	public void binaryTest() throws IOException {
		Assert.assertEquals("asd", IOUtils.toString((InputStream) map.get("binary")));
	}

	@Test
	public void nullableTest() {
		Assert.assertNull(map.get("nullable"));
	}

	@Test
	public void testPropertyOverwritten() throws PersistenceException {
		TestClass anotherTestClass = new TestClass();
		anotherTestClass.intObject = 7777777;

		modelPersister.persist(anotherTestClass, "nativePropertyTest", resolver.getResource("/"));
		resolver.commit();
		Resource overwrittenResource = resolver.getResource("/nativePropertyTest");
		ValueMap overwrittenMap = overwrittenResource.adaptTo(ValueMap.class);

		Assert.assertEquals(7777777L, overwrittenMap.get("intObject")); // integer are transformed into longs
	}

	@Test
	public void testPropertyOverwrittenByNull() throws PersistenceException {
		TestClass anotherTestClass = new TestClass();
		anotherTestClass.intObject = null;

		modelPersister.persist(anotherTestClass, "nativePropertyTest", resolver.getResource("/"));
		resolver.commit();
		Resource overwrittenResource = resolver.getResource("/nativePropertyTest");
		ValueMap overwrittenMap = overwrittenResource.adaptTo(ValueMap.class);

		Assert.assertFalse(overwrittenMap.containsKey("intObject"));
	}

	@Test
	public void testOverwriteWithDifferentClass() throws PersistenceException {
		AnotherTestClass anotherTestClass = new AnotherTestClass();

		modelPersister.persist(anotherTestClass, "nativePropertyTest", resolver.getResource("/"));
		resolver.commit();

		Resource overwrittenResource = resolver.getResource("/nativePropertyTest");
		ValueMap overwrittenMap = overwrittenResource.adaptTo(ValueMap.class);

		// TODO Saving different class at same path does not clear old properties
		//Assert.assertFalse("should NOT have old property", overwrittenMap.containsKey("intObject"));
		Assert.assertEquals("should have new property", "asd", overwrittenMap.get("prop"));
	}
}
