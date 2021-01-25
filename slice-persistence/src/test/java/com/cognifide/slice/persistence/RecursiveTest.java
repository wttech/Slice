/*-
 * #%L
 * Slice - Persistence
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
package com.cognifide.slice.persistence;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.cognifide.slice.mapper.annotation.JcrProperty;
import com.cognifide.slice.mapper.annotation.SliceResource;

public class RecursiveTest extends BaseTest {

	@SliceResource
	@SuppressWarnings("unused")
	private static class TestClass {

		@JcrProperty
		private TestClass testClass;

		@JcrProperty("differentName")
		private String someName = "asd";

		@JcrProperty
		private String nullField;

		@JcrProperty
		private SubClass subClass = new SubClass();

		private String fieldWithoutAnnotation = "asd";

		public TestClass() {
			this.testClass = this;
		}
	}

	@SliceResource
	private static class SubClass {

		@JcrProperty
		private String prop = "123";

	}

	private ValueMap map;

	private Resource resource;

	@Before
	public void beforeTest() throws LoginException, PersistenceException, IllegalAccessException {
		modelPersister.persist(new TestClass(), "recursiveTest", resolver.getResource("/"));
		resolver.commit();
		resource = resolver.getResource("/recursiveTest");
		map = resource.adaptTo(ValueMap.class);
	}

	@Test
	public void testOverridenName() {
		Assert.assertFalse(map.containsKey("someName"));
		Assert.assertEquals("asd", map.get("differentName"));
	}

	@Test
	public void testNullField() {
		Assert.assertFalse(map.containsKey("nullField"));
	}

	@Test
	public void testNoAnnotation() {
		Assert.assertFalse(map.containsKey("fieldWithoutAnnotation"));
	}

	@Test
	public void testSubResource() {
		Assert.assertEquals("123", map.get("subClass/prop"));
	}

	@Test
	public void testAlreadyVisited() {
		Assert.assertNull(resource.getChild("testClass"));
	}

	@Test
	public void testOverwrittenProperty() throws PersistenceException {
		TestClass anotherTestClass = new TestClass();
		anotherTestClass.subClass = new SubClass();
		anotherTestClass.someName = "qwe";

		modelPersister.persist(anotherTestClass, "recursiveTest", resolver.getResource("/"));
		resolver.commit();
		Resource overwrittenResource = resolver.getResource("/recursiveTest");
		ValueMap overwrittenMap = overwrittenResource.adaptTo(ValueMap.class);

		Assert.assertEquals("qwe", overwrittenMap.get("differentName"));
	}

	@Test
	public void testOverwrittenSubClassProperty() throws PersistenceException {
		TestClass anotherTestClass = new TestClass();
		anotherTestClass.subClass = new SubClass();
		anotherTestClass.subClass.prop = "456";

		modelPersister.persist(anotherTestClass, "recursiveTest", resolver.getResource("/"));
		resolver.commit();
		Resource overwrittenResource = resolver.getResource("/recursiveTest");
		ValueMap overwrittenMap = overwrittenResource.adaptTo(ValueMap.class);

		Assert.assertEquals("456", overwrittenMap.get("subClass/prop"));
	}

	@Test
	public void testNullSubClass() throws PersistenceException {
		TestClass anotherTestClass = new TestClass();
		anotherTestClass.subClass = null;

		modelPersister.persist(anotherTestClass, "recursiveTest", resolver.getResource("/"));
		resolver.commit();
		Resource overwrittenResource = resolver.getResource("/recursiveTest");
		ValueMap overwrittenMap = overwrittenResource.adaptTo(ValueMap.class);

		Assert.assertFalse(overwrittenMap.containsKey("subClass"));
	}

	@Test
	public void testOverwriteWithDifferentClass() throws PersistenceException {
		SubClass anotherTestClass = new SubClass();
		modelPersister.persist(anotherTestClass, "recursiveTest", resolver.getResource("/"));
		resolver.commit();

		Resource overwrittenResource = resolver.getResource("/recursiveTest");
		ValueMap overwrittenMap = overwrittenResource.adaptTo(ValueMap.class);

		Assert.assertFalse("should NOT have old property", overwrittenMap.containsKey("fieldWithoutAnnotation"));
		Assert.assertEquals("should have new class property", "123", map.get("prop"));
	}
}
