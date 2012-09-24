package com.cognifide.slice.cq.model;

/*
 * #%L
 * Slice - CQ Add-on
 * $Id:$
 * $HeadURL:$
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


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.cognifide.slice.cq.model.WcmModeModel;
import com.day.cq.wcm.api.WCMMode;

public class WcmModeTest {
	@Test
	public void testDisabled() {
		WcmModeModel wcmMode;
		wcmMode = new WcmModeModel(WCMMode.DISABLED);
		assertTrue(wcmMode.isDisabled());
		assertFalse(wcmMode.isEdit());
		assertFalse(wcmMode.isDesign());
		assertFalse(wcmMode.isPreview());
		assertFalse(wcmMode.isReadOnly());
		assertFalse(wcmMode.isAuthor());
		assertTrue(wcmMode.isPublish());
	}

	@Test
	public void testEdit() {
		WcmModeModel wcmMode;
		wcmMode = new WcmModeModel(WCMMode.EDIT);
		assertFalse(wcmMode.isDisabled());
		assertTrue(wcmMode.isEdit());
		assertFalse(wcmMode.isDesign());
		assertFalse(wcmMode.isPreview());
		assertFalse(wcmMode.isReadOnly());
		assertTrue(wcmMode.isAuthor());
		assertFalse(wcmMode.isPublish());
	}

	@Test
	public void testDesign() {
		WcmModeModel wcmMode;
		wcmMode = new WcmModeModel(WCMMode.DESIGN);
		assertFalse(wcmMode.isDisabled());
		assertFalse(wcmMode.isEdit());
		assertTrue(wcmMode.isDesign());
		assertFalse(wcmMode.isPreview());
		assertFalse(wcmMode.isReadOnly());
		assertTrue(wcmMode.isAuthor());
		assertFalse(wcmMode.isPublish());
	}

	@Test
	public void testPreview() {
		WcmModeModel wcmMode;
		wcmMode = new WcmModeModel(WCMMode.PREVIEW);
		assertFalse(wcmMode.isDisabled());
		assertFalse(wcmMode.isEdit());
		assertFalse(wcmMode.isDesign());
		assertTrue(wcmMode.isPreview());
		assertFalse(wcmMode.isReadOnly());
		assertFalse(wcmMode.isAuthor());
		assertTrue(wcmMode.isPublish());
	}

	@Test
	public void testReadOnly() {
		WcmModeModel wcmMode;
		wcmMode = new WcmModeModel(WCMMode.READ_ONLY);
		assertFalse(wcmMode.isDisabled());
		assertFalse(wcmMode.isEdit());
		assertFalse(wcmMode.isDesign());
		assertFalse(wcmMode.isPreview());
		assertTrue(wcmMode.isReadOnly());
		assertFalse(wcmMode.isAuthor());
		assertTrue(wcmMode.isPublish());
	}
}
