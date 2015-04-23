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