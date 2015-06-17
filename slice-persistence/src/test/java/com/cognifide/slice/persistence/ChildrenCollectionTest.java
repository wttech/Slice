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

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.cognifide.slice.mapper.annotation.Children;
import com.cognifide.slice.mapper.annotation.JcrProperty;
import com.cognifide.slice.mapper.annotation.SliceResource;

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
	}

	@SliceResource
	private static class SubClass {

		@JcrProperty
		private String prop;

		public SubClass(String prop) {
			this.prop = prop;
		}

	}

	private ValueMap map;

	@Before
	public void beforeTest() throws LoginException, PersistenceException, IllegalAccessException {
		persistence.persist(new TestClass(), "childrenColl", resolver.getResource("/"));
		resolver.commit();
		final Resource resource = resolver.getResource("/childrenColl");
		map = resource.adaptTo(ValueMap.class);
	}

	@Test
	public void testList() {
		for (int i = 0; i < 10; i++) {
			Assert.assertEquals("value no " + i, map.get("testList/testList_" + (i + 1) + "/prop"));
		}
	}

	@Test
	public void testArray() {
		for (int i = 0; i < 10; i++) {
			Assert.assertEquals("arr value no " + i, map.get("testArray/testArray_" + (i + 1) + "/prop"));
		}
	}

}
