package com.cognifide.slice.mapper.impl.processor;

import java.util.Collection;
import java.util.Iterator;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ListFieldProcessorTest {

	@Mock
	private Resource resource;

	@Mock
	private ValueMap valueMap;

	String propertyName;

	ListFieldProcessor processor;

	@Before
	public void setUp() {
		processor = new ListFieldProcessor();
		propertyName = "prop";
	}

	@Test
	public void testMapResourceToField() {
		String[] resultArray = new String[] { "asd", "qwe" };
		Mockito.when(valueMap.get(propertyName)).thenReturn(resultArray);

		Object result = processor.mapResourceToField(resource, valueMap, null, propertyName);

		Assert.assertTrue(result instanceof Collection);
		Collection<?> coll = (Collection<?>) result;
		Assert.assertEquals(2, coll.size());
		Iterator<?> iterator = coll.iterator();
		Assert.assertEquals("asd", iterator.next());
		Assert.assertEquals("qwe", iterator.next());
	}

	@Test
	public void testMapResourceToField_emptyList() {
		String[] resultArray = new String[] {};
		Mockito.when(valueMap.get(propertyName)).thenReturn(resultArray);

		Object result = processor.mapResourceToField(resource, valueMap, null, propertyName);

		Assert.assertTrue(result instanceof Collection);
		Collection<?> coll = (Collection<?>) result;
		Assert.assertEquals(0, coll.size());
	}

	@Test
	public void testMapResourceToField_notArray() {
		String resultProperty = "result";
		Mockito.when(valueMap.get(propertyName)).thenReturn(resultProperty);

		Object result = processor.mapResourceToField(resource, valueMap, null, propertyName);

		Assert.assertTrue(result instanceof Collection);
		Collection<?> coll = (Collection<?>) result;
		Assert.assertEquals(1, coll.size());
		Assert.assertEquals("result", coll.iterator().next());
	}
	
	@Test
	public void testMapResourceToField_nullValue() {
		String resultProperty = "result";
		Mockito.when(valueMap.get(propertyName)).thenReturn(resultProperty);
		
		Object result = processor.mapResourceToField(resource, null, null, propertyName);
		Assert.assertNull(result);
		
		Mockito.when(valueMap.get(propertyName)).thenReturn(null);
		result = processor.mapResourceToField(resource, valueMap, null, propertyName);
		Assert.assertNull(result);
	}

}
