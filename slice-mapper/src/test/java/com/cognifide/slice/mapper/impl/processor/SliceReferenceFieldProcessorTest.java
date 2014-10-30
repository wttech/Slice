/*-
 * #%L
 * Slice - Mapper
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

package com.cognifide.slice.mapper.impl.processor;

import java.lang.reflect.Field;

import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SliceReferenceFieldProcessorTest {

	@Mock
	private Resource resource;

	private SliceReferenceFieldProcessor processor;

	@Before
	public void setUp() throws SecurityException, NoSuchFieldException {
		processor = new SliceReferenceFieldProcessor();
	}

	@Test
	public void testAcceptString() throws SecurityException, NoSuchFieldException {
		Field field = TestObject.class.getDeclaredField("stringField");
		boolean result = processor.accepts(resource, field);
		Assert.assertFalse("String field should not be acceptable", result);
	}

	@Test
	public void testAcceptPrimitive() throws SecurityException, NoSuchFieldException {
		Field field = TestObject.class.getDeclaredField("booleanField");
		boolean result = processor.accepts(resource, field);
		Assert.assertFalse("Primitive field should not be acceptable", result);

		field = TestObject.class.getDeclaredField("intField");
		result = processor.accepts(resource, field);
		Assert.assertFalse("Primitive field should not be acceptable", result);
	}

	@Test
	public void testAcceptAnnotated() throws SecurityException, NoSuchFieldException {
		Field field = TestObject.class.getDeclaredField("fieldAnnotatedWithSliceReference");
		boolean result = processor.accepts(resource, field);
		Assert.assertTrue("Annotated field should be acceptable", result);
	}
}
