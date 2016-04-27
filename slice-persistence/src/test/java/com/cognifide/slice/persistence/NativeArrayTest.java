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

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.cognifide.slice.mapper.annotation.JcrProperty;
import com.cognifide.slice.mapper.annotation.SliceResource;

public class NativeArrayTest extends BaseTest {

	@SliceResource
	private static class TestClass {

		@JcrProperty
		private long[] longArray = new long[] { 1, 2, 3 };

		@JcrProperty
		private Long[] longObjectArray = new Long[] { 1l, 2l, 3l };
	}

	private ValueMap map;

	@Before
	public void beforeTest() throws LoginException, PersistenceException, IllegalAccessException {
		modelPersister.persist(new TestClass(), "nativeArray", resolver.getResource("/"));
		resolver.commit();
		map = resolver.getResource("/nativeArray").adaptTo(ValueMap.class);
	}

	@Test
	public void primitiveArrayTest() {
		Assert.assertArrayEquals(new Long[] { 1l, 2l, 3l }, (Long[]) map.get("longArray"));
	}

	@Test
	public void objectArrayTest() {
		Assert.assertArrayEquals(new Long[] { 1l, 2l, 3l }, (Long[]) map.get("longObjectArray"));
	}

	@Test
	public void overwriteTest() throws PersistenceException {
		TestClass anotherTestClass = new TestClass();
		anotherTestClass.longArray = new long[] { -4444 };
		anotherTestClass.longObjectArray = new Long[] { -55555L };

		modelPersister.persist(anotherTestClass, "nativeArray", resolver.getResource("/"));
		resolver.commit();
		Resource anotherResource = resolver.getResource("/nativeArray");
		ValueMap anotherMap = anotherResource.adaptTo(ValueMap.class);

		Assert.assertArrayEquals(new Long[] { -4444L }, (Long[]) anotherMap.get("longArray"));
		Assert.assertArrayEquals(new Long[] { -55555L }, (Long[]) anotherMap.get("longObjectArray"));
	}

	@Test
	public void overwriteByNullTest() throws PersistenceException {
		TestClass anotherTestClass = new TestClass();
		anotherTestClass.longArray = null;
		anotherTestClass.longObjectArray = null;

		modelPersister.persist(anotherTestClass, "nativeArray", resolver.getResource("/"));
		resolver.commit();
		Resource anotherResource = resolver.getResource("/nativeArray");
		ValueMap anotherMap = anotherResource.adaptTo(ValueMap.class);

		Assert.assertFalse(anotherMap.containsKey("longArray"));
		Assert.assertFalse(anotherMap.containsKey("longObjectArray"));
	}

}
