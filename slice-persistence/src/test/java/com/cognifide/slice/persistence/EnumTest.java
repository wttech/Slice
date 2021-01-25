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

public class EnumTest extends BaseTest {

	private enum TestEnum {

		E1, E2;

		@Override
		public String toString() {
			return "this is enum value: " + name();
		}
	}

	@SliceResource
	private static class TestClass {

		@JcrProperty
		private TestEnum myEnum = TestEnum.E1;

	}

	private ValueMap map;

	@Before
	public void beforeTest() throws LoginException, PersistenceException, IllegalAccessException {
		modelPersister.persist(new TestClass(), "enumTest", resolver.getResource("/"));
		resolver.commit();
		map = resolver.getResource("/enumTest").adaptTo(ValueMap.class);
	}

	@Test
	public void enumTest() {
		Assert.assertEquals("E1", map.get("myEnum"));
	}

	@Test
	public void enumOverwriteTest() throws PersistenceException {
		TestClass anothertestClass = new TestClass();
		anothertestClass.myEnum = TestEnum.E2;

		modelPersister.persist(anothertestClass, "enumTest", resolver.getResource("/"));
		resolver.commit();
		Resource anotherResource = resolver.getResource("/enumTest");
		ValueMap anotherMap = anotherResource.adaptTo(ValueMap.class);

		Assert.assertEquals("E2", anotherMap.get("myEnum"));
	}

	@Test
	public void enumOverwriteByNullTest() throws PersistenceException {
		TestClass anothertestClass = new TestClass();
		anothertestClass.myEnum = null;

		modelPersister.persist(anothertestClass, "enumTest", resolver.getResource("/"));
		resolver.commit();
		Resource anotherResource = resolver.getResource("/enumTest");
		ValueMap anotherMap = anotherResource.adaptTo(ValueMap.class);

		Assert.assertFalse(anotherMap.containsKey("myEnum"));
	}

}
