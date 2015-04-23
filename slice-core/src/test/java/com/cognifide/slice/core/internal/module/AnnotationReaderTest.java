/*-
 * #%L
 * Slice - Core
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
package com.cognifide.slice.core.internal.module;

import com.cognifide.slice.mapper.annotation.JcrProperty;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class AnnotationReaderTest {

	private AnnotationReader annotationReader;

	@Before
	public void setUp() throws Exception {
		annotationReader = new AnnotationReader();
	}

	@Test
	public void testVisit() throws Exception {
		annotationReader.visit(1, 1, "string1", "string2", "string3", new String[] {});
		Assert.assertEquals(annotationReader.getAnnotations().size(), 0);
	}

	@Test
	public void testVisitAnnotation() throws Exception {
		annotationReader.visitAnnotation("/com/cognifide/slice/mapper/annotation/JcrProperty/", false);
		Assert.assertEquals(annotationReader.getAnnotations().size(), 1);
		Assert.assertEquals(annotationReader.getAnnotations().get(0),
				"com.cognifide.slice.mapper.annotation.JcrProperty");
	}

	@Test
	public void testIsAnnotationPresent() throws Exception {
		Assert.assertFalse(annotationReader.isAnnotationPresent(JcrProperty.class));
		Assert.assertEquals(annotationReader.getAnnotations().size(), 0);
		annotationReader.visitAnnotation("/com/cognifide/slice/mapper/annotation/JcrProperty/", false);
		Assert.assertEquals(annotationReader.getAnnotations().size(), 1);
		Assert.assertTrue(annotationReader.isAnnotationPresent(JcrProperty.class));
	}
}