package com.cognifide.slice.cq.utils;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class PathUtilsTest {

	private PathUtils pathUtils = new PathUtils();

	@Test
	public void testGetPagePathFromResourcePath() {
		Assert.assertEquals(StringUtils.EMPTY, pathUtils.getPagePathFromResourcePath(""));

		Assert.assertEquals("page", pathUtils.getPagePathFromResourcePath("page"));

		Assert.assertEquals("/content/page/component", pathUtils.getPagePathFromResourcePath("/content/page/component"));

		Assert.assertEquals("/content/page", pathUtils.getPagePathFromResourcePath("/content/page/jcr:content/component"));
	}
}
