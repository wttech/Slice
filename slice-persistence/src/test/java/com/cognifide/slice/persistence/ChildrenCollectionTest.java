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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Test;

import com.cognifide.slice.mapper.annotation.Children;
import com.cognifide.slice.mapper.annotation.JcrProperty;
import com.cognifide.slice.mapper.annotation.SliceResource;
import com.google.common.collect.Lists;

public class ChildrenCollectionTest extends BaseTest {

	@SliceResource
	private static class TestClass {

		@JcrProperty
		@Children(SubClass.class)
		private List<SubClass> testList;

		@JcrProperty
		@Children(SubClass.class)
		private SubClass[] testArray;

		public TestClass() {
			this.testList = new ArrayList<SubClass>();
			this.testArray = new SubClass[10];
			for (int i = 0; i < 10; i++) {
				testList.add(new SubClass("value no " + i));
				testArray[i] = new SubClass("arr value no " + i);
			}
		}

		public void removeFromList(int count) {
			this.testList = testList.subList(0, testList.size() - count);
		}

		public void removeFromArray(int count) {
			this.testArray = Arrays.copyOfRange(testArray, 0, testArray.length - count);
		}
	}

	@SliceResource
	private static class SubClass {

		@JcrProperty
		private String prop;

		public SubClass(String prop) {
			this.prop = prop;
		}

	}

	@Test
	public void testList() throws PersistenceException {
		final TestClass test = new TestClass();

		// Check saving elements
		modelPersister.persist(test, "childrenColl", resolver.getResource("/"));
		resolver.commit();

		final Resource resource = resolver.getResource("/childrenColl");
		final ValueMap map = resource.adaptTo(ValueMap.class);

		assertNotNull(map);

		for (int i = 0; i < 10; i++) {
			assertEquals("value no " + i, map.get("testList/testList_" + (i + 1) + "/prop"));
		}
	}

	@Test
	public void testListOverwrite() throws PersistenceException {
		final TestClass test = new TestClass();
		modelPersister.persist(test, "childrenColl", resolver.getResource("/"));
		resolver.commit();

		// removes last 3 elements from list
		test.removeFromList(3);
		modelPersister.persist(test, "childrenColl", resolver.getResource("/"));
		resolver.commit();

		final ArrayList<Resource> subClasses = Lists
				.newArrayList(resolver.getResource("/childrenColl/testList").listChildren());
		assertEquals(7, subClasses.size());
	}

	@Test
	public void testListOverwriteByNull() throws PersistenceException {
		final TestClass test = new TestClass();
		modelPersister.persist(test, "childrenColl", resolver.getResource("/"));
		resolver.commit();

		test.testList = null;
		modelPersister.persist(test, "childrenColl", resolver.getResource("/"));
		resolver.commit();

		final Resource resource = resolver.getResource("/childrenColl");
		assertNull("testList property should be null", resource.getChild("testList"));
	}

	@Test
	public void testArray() throws PersistenceException {
		final TestClass test = new TestClass();

		// Check saving elements
		modelPersister.persist(test, "childrenColl", resolver.getResource("/"));
		resolver.commit();

		final Resource resource = resolver.getResource("/childrenColl");
		final ValueMap map = resource.adaptTo(ValueMap.class);

		assertNotNull(map);

		for (int i = 0; i < 10; i++) {
			assertEquals("arr value no " + i, map.get("testArray/testArray_" + (i + 1) + "/prop"));
		}
	}

	@Test
	public void testArrayOverwrite() throws PersistenceException {
		final TestClass test = new TestClass();
		modelPersister.persist(test, "childrenColl", resolver.getResource("/"));
		resolver.commit();

		// removes last 5 elements from list
		test.removeFromArray(5);
		modelPersister.persist(test, "childrenColl", resolver.getResource("/"));
		resolver.commit();

		final ArrayList<Resource> subClasses = Lists
				.newArrayList(resolver.getResource("/childrenColl/testArray").listChildren());
		assertEquals(5, subClasses.size());
	}

	@Test
	public void testArrayOverwriteByNull() throws PersistenceException {
		final TestClass test = new TestClass();
		modelPersister.persist(test, "childrenColl", resolver.getResource("/"));
		resolver.commit();

		test.testArray = null;
		modelPersister.persist(test, "childrenColl", resolver.getResource("/"));
		resolver.commit();

		Resource resource = resolver.getResource("/childrenColl");
		assertNull("testArray property should be null", resource.getChild("testArray"));
	}

}
