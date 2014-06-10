package com.cognifide.slice.mapper.impl.processor;

import java.lang.reflect.Field;

import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

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
