package com.cognifide.slice.util;

import org.junit.Test;

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
}
