/*-
 * #%L
 * Slice - Core API
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2012 - 2014 Cognifide Limited
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
