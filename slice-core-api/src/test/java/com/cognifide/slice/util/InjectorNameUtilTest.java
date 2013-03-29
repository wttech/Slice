package com.cognifide.slice.util;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class InjectorNameUtilTest {
	// /////////////////////////////////////////////////////////////////////////
	// getFromResourceType
	// ///////////////////////////////////////////////////////////////////////

	@Test
	public void testForResourceType() {
		// border conditions
		testForSingleInvalidResourceType(null);
		testForSingleInvalidResourceType("");
		testForSingleInvalidResourceType("/");
		testForSingleInvalidResourceType("/apps");
		testForSingleInvalidResourceType("/apps/");

		// absolute resource
		testForSingleResourceType("/libs/test");
		testForSingleResourceType("/apps/test");
		testForSingleResourceType("/apps/test/");
		testForSingleResourceType("/apps/test/components");
		testForSingleResourceType("/apps/test/components/page");

		// relative resource
		testForSingleResourceType("test");
		testForSingleResourceType("test/");
		testForSingleResourceType("test/components");
		testForSingleResourceType("test/components/page");
	}

	private void testForSingleInvalidResourceType(String resourceType) {
		String name = InjectorNameUtil.getFromResourceType(resourceType);
		assertNull("Expected null for invalid resource type", name);
	}

	private void testForSingleResourceType(String resourceType) {
		String name = InjectorNameUtil.getFromResourceType(resourceType);
		assertEquals("test", name);
	}

	// /////////////////////////////////////////////////////////////////////////
	// getForResource -- plain resource
	// ///////////////////////////////////////////////////////////////////////

	@Test
	public void testForPlainResource() {
		// border conditions
		String name = InjectorNameUtil.getForResource(null);
		assertNull("Expected null for invalid resource", name);
		testForSingleInvalidPlainResource("");
		testForSingleInvalidPlainResource("/");
		testForSingleInvalidPlainResource("/apps");
		testForSingleInvalidPlainResource("/apps/");

		// absolute resource
		testForSinglePlainResource("/libs/test");
		testForSinglePlainResource("/apps/test");
		testForSinglePlainResource("/apps/test/");
		testForSinglePlainResource("/apps/test/components");
		testForSinglePlainResource("/apps/test/components/page");

		// relative resource
		testForSinglePlainResource("test");
		testForSinglePlainResource("test/");
		testForSinglePlainResource("test/components");
		testForSinglePlainResource("test/components/page");
	}

	private void testForSingleInvalidPlainResource(String resourceType) {
		Resource resource = getPlainResource(resourceType);
		String name = InjectorNameUtil.getForResource(resource);
		assertNull("Expected null for invalid resource", name);
	}

	private void testForSinglePlainResource(String resourceType) {
		Resource resource = getPlainResource(resourceType);
		String name = InjectorNameUtil.getForResource(resource);
		assertEquals("test", name);
	}

	private Resource getPlainResource(String resourceType) {
		Resource resource = Mockito.mock(Resource.class);
		when(resource.getResourceType()).thenReturn(resourceType);
		when(resource.getParent()).thenReturn(null);
		return resource;
	}

	// /////////////////////////////////////////////////////////////////////////
	// getForResource -- resource with property
	// ///////////////////////////////////////////////////////////////////////

	@Test
	public void testForResourceWithProperty() {
		Resource resource = getResourceWithInjectorNameProperty(null, null);
		String name = InjectorNameUtil.getForResource(resource);
		assertNull("Expected null for empty resource type and injector name property", name);

		resource = getResourceWithInjectorNameProperty("/apps/test", null);
		name = InjectorNameUtil.getForResource(resource);
		assertEquals("With empty property, should fallback to resource type", "test", name);

		resource = getResourceWithInjectorNameProperty("/apps/notTest", "test");
		name = InjectorNameUtil.getForResource(resource);
		assertEquals("Injector name approach should take priority over resource type", "test", name);

		resource = getResourceWithInjectorNamePropertyInContent(null, null);
		name = InjectorNameUtil.getForResource(resource);
		assertNull("Expected null for empty resource type and injector name property", name);

		resource = getResourceWithInjectorNamePropertyInContent("/apps/notTest", "test");
		name = InjectorNameUtil.getForResource(resource);
		assertEquals("Injector name approach should take priority over resource type", "test", name);
	}

	private Resource getResourceWithInjectorNameProperty(String resourceType, String injectorName) {
		Resource resource = Mockito.mock(Resource.class);
		when(resource.getResourceType()).thenReturn(resourceType);
		when(resource.getParent()).thenReturn(null);

		ValueMap properties = Mockito.mock(ValueMap.class);
		when(properties.get("injectorName", String.class)).thenReturn(injectorName);

		when(resource.adaptTo(ValueMap.class)).thenReturn(properties);
		return resource;
	}

	private Resource getResourceWithInjectorNamePropertyInContent(String resourceType, String injectorName) {
		Resource resource = getResourceWithInjectorNameProperty(null, null);
		Resource jcrContent = getResourceWithInjectorNameProperty(resourceType, injectorName);
		when(resource.getChild("jcr:content")).thenReturn(jcrContent);
		return resource;
	}

	// /////////////////////////////////////////////////////////////////////////
	// getForResource -- resource with ancestors with property
	// ///////////////////////////////////////////////////////////////////////

	@Test
	public void testForResourceWithAncestorsWithProperty() {
		Resource resource = getPlainResource("/apps/notTest/components");
		Resource parent = getPlainResource("/apps/notTest/components");
		Resource grandParent = getResourceWithInjectorNameProperty("/apps/notTest/components", "test");
		when(resource.getParent()).thenReturn(parent);
		when(parent.getParent()).thenReturn(grandParent);
		String name = InjectorNameUtil.getForResource(resource);
		assertEquals("test", name);

		resource = getPlainResource("/apps/notTest/components");
		parent = getPlainResource("/apps/notTest/components");
		grandParent = getResourceWithInjectorNamePropertyInContent("/apps/notTest/components", "test");
		when(resource.getParent()).thenReturn(parent);
		when(parent.getParent()).thenReturn(grandParent);
		name = InjectorNameUtil.getForResource(resource);
		assertEquals("test", name);
	}
}
