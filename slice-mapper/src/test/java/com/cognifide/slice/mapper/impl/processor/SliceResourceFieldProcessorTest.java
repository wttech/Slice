package com.cognifide.slice.mapper.impl.processor;

import java.lang.reflect.Field;

import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

public class SliceResourceFieldProcessorTest {

	@Mock
	private Resource resource;

	private SliceResourceFieldProcessor processor;

	@Test
	public void testAccept() throws SecurityException, NoSuchFieldException {
		processor = new SliceResourceFieldProcessor();
	}

	public void testAcceptString() throws SecurityException, NoSuchFieldException {
		Field field = TestObject.class.getDeclaredField("stringField");
		boolean result = processor.accepts(resource, field);
		Assert.assertFalse("String field should not be acceptable", result);
	}

	public void testAcceptPrimitive() throws SecurityException, NoSuchFieldException {
		Field field = TestObject.class.getDeclaredField("booleanField");
		boolean result = processor.accepts(resource, field);
		Assert.assertFalse("Primitive field should not be acceptable", result);

		field = TestObject.class.getDeclaredField("intField");
		result = processor.accepts(resource, field);
		Assert.assertFalse("Primitive field should not be acceptable", result);
	}

	public void testAcceptAnnotated() throws SecurityException, NoSuchFieldException {
		Field field = TestObject.class.getDeclaredField("annotatedField");
		boolean result = processor.accepts(resource, field);
		Assert.assertTrue("Annotated field should be acceptable", result);
	}

}
