package com.cognifide.slice.core.internal.link;

/*-
 * #%L
 * Slice - Core
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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.cognifide.slice.commons.link.MockResourceResolver;

public class SlingPathDecomposerTest {

	@Test(expected = NullPointerException.class)
	public void testNullUrl() {
		new SlingPathDecomposer(null, null);
	}

	@Test
	public void testGetResolutionPathInfo() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/some/path/here.html/something",
				new MockResourceResolver("/some/path/here"));
		assertNotNull(spd.getResource());
		assertEquals("/some/path/here", spd.getResourcePath());
		assertArrayEquals(new String[] {}, spd.getSelectors());
		assertEquals("html", spd.getExtension());
		assertEquals("/something", spd.getSuffix());
	}

	@Test
	public void testUrlExistingResourcePathOnly() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/a/b", new MockResourceResolver("/a/b"));
		assertNotNull(spd.getResource());
		assertEquals("/a/b", spd.getResourcePath());
		assertEquals("/a/b", spd.getResource().getPath());
		assertArrayEquals(new String[] {}, spd.getSelectors());
		assertEquals("", spd.getSelectorString());
		assertEquals("", spd.getExtension());
		assertEquals("", spd.getSuffix());
	}

	@Test
	public void testUrlNotExistingResourcePathOnly() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/a/b/c", new MockResourceResolver("/a/b"));
		assertNull(spd.getResource());
		assertEquals("/a/b/c", spd.getResourcePath());
		assertArrayEquals(new String[] {}, spd.getSelectors());
		assertEquals("", spd.getSelectorString());
		assertEquals("", spd.getExtension());
		assertEquals("", spd.getSuffix());
	}

	@Test
	public void testUrlWithExtension() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/a/b.html", new MockResourceResolver("/a/b"));
		assertNotNull(spd.getResource());
		assertEquals("/a/b", spd.getResourcePath());
		assertEquals("/a/b", spd.getResource().getPath());
		assertArrayEquals(new String[] {}, spd.getSelectors());
		assertEquals("", spd.getSelectorString());
		assertEquals("html", spd.getExtension());
		assertEquals("", spd.getSuffix());
	}

	@Test
	public void testResourcePathWithSelectorAndExtenstion() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/a/b.s1.html", new MockResourceResolver("/a/b"));
		assertNotNull(spd.getResource());
		assertEquals("/a/b", spd.getResourcePath());
		assertEquals("/a/b", spd.getResource().getPath());
		assertEquals(1, spd.getSelectors().length);
		assertEquals("s1", spd.getSelectorString());
		assertEquals("s1", spd.getSelectors()[0]);
		assertEquals("html", spd.getExtension());
		assertEquals("", spd.getSuffix());
	}

	@Test
	public void testResourcePathWithTwoSelectorAndExtenstion() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/a/b.s1.s2.html", new MockResourceResolver("/a/b"));
		assertNotNull(spd.getResource());
		assertEquals("/a/b", spd.getResourcePath());
		assertEquals("/a/b", spd.getResource().getPath());
		assertEquals(2, spd.getSelectors().length);
		assertEquals("s1.s2", spd.getSelectorString());
		assertEquals("s1", spd.getSelectors()[0]);
		assertEquals("s2", spd.getSelectors()[1]);
		assertEquals("html", spd.getExtension());
		assertEquals("", spd.getSuffix());
	}

	@Test
	public void testResourceExistingWithSuffixOnly() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/a/b./c/d", new MockResourceResolver("/a/b"));
		assertNotNull(spd.getResource());
		assertEquals("/a/b", spd.getResourcePath());
		assertEquals(0, spd.getSelectors().length);
		assertEquals("", spd.getSelectorString());
		assertEquals("", spd.getExtension());
		assertEquals("/c/d", spd.getSuffix());
	}

	@Test
	public void testResourceExistingWithExtensionWithSuffix() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/a/b.html/c/d", new MockResourceResolver("/a/b"));
		assertNotNull(spd.getResource());
		assertEquals("/a/b", spd.getResourcePath());
		assertEquals(0, spd.getSelectors().length);
		assertEquals("", spd.getSelectorString());
		assertEquals("html", spd.getExtension());
		assertEquals("/c/d", spd.getSuffix());
	}

	@Test
	public void testResourceExistingWithSelectorWithExtensionWithSuffix() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/a/b.s1.html/c/d",
				new MockResourceResolver("/a/b"));
		assertNotNull(spd.getResource());
		assertEquals("/a/b", spd.getResourcePath());
		assertEquals(1, spd.getSelectors().length);
		assertEquals("s1", spd.getSelectors()[0]);
		assertEquals("s1", spd.getSelectorString());
		assertEquals("html", spd.getExtension());
		assertEquals("/c/d", spd.getSuffix());
	}

	@Test
	public void testResourceExistingWithTwoSelectorWithExtensionWithSuffix() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/a/b.s1.s2.html/c/d", new MockResourceResolver(
				"/a/b"));
		assertNotNull(spd.getResource());
		assertEquals("/a/b", spd.getResourcePath());
		assertEquals(2, spd.getSelectors().length);
		assertEquals("s1", spd.getSelectors()[0]);
		assertEquals("s2", spd.getSelectors()[1]);
		assertEquals("s1.s2", spd.getSelectorString());
		assertEquals("html", spd.getExtension());
		assertEquals("/c/d", spd.getSuffix());
	}

	@Test
	public void testNotExistingResourceExistingWithTwoSelectorWithExtensionWithSuffix() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/a/b/c/d.s.txt", new MockResourceResolver("/a/b"));
		assertNull(spd.getResource());
		assertEquals("/a/b/c/d", spd.getResourcePath());
		assertEquals(1, spd.getSelectors().length);
		assertEquals("s", spd.getSelectors()[0]);
		assertEquals("s", spd.getSelectorString());
		assertEquals("txt", spd.getExtension());
		assertEquals("", spd.getSuffix());
	}

	@Test
	public void testExistingResourceExistingWithoutSelectorWithExtensionWithSuffix() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/a/b.html/c/d.s.txt", new MockResourceResolver(
				"/a/b"));
		assertNotNull(spd.getResource());
		assertEquals("/a/b", spd.getResource().getPath());
		assertEquals("/a/b", spd.getResourcePath());
		assertEquals(0, spd.getSelectors().length);
		assertEquals("", spd.getSelectorString());
		assertEquals("html", spd.getExtension());
		assertEquals("/c/d.s.txt", spd.getSuffix());
	}

	@Test
	public void testExistingResourceExistingWithoutSelectorWithExtensionWithSuffix2() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/a/b.s1.html/c/d.s.txt", new MockResourceResolver(
				"/a/b"));
		assertNotNull(spd.getResource());
		assertEquals("/a/b", spd.getResource().getPath());
		assertEquals("/a/b", spd.getResourcePath());
		assertEquals(1, spd.getSelectors().length);
		assertEquals("s1", spd.getSelectors()[0]);
		assertEquals("s1", spd.getSelectorString());
		assertEquals("html", spd.getExtension());
		assertEquals("/c/d.s.txt", spd.getSuffix());
	}

	@Test
	public void testExistingResourceExistingWithoutSelectorWithExtensionWithSuffix3() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/a/b.s1.s2.html/c/d.s.txt",
				new MockResourceResolver("/a/b"));
		assertNotNull(spd.getResource());
		assertEquals("/a/b", spd.getResource().getPath());
		assertEquals("/a/b", spd.getResourcePath());
		assertEquals(2, spd.getSelectors().length);
		assertEquals("s1", spd.getSelectors()[0]);
		assertEquals("s2", spd.getSelectors()[1]);
		assertEquals("s1.s2", spd.getSelectorString());
		assertEquals("html", spd.getExtension());
		assertEquals("/c/d.s.txt", spd.getSuffix());
	}

	// tests taken from Sling:

	@Test
	public void testTrailingDot() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/some/path.", new MockResourceResolver(
				"/some/path"));
		assertEquals("/some/path", spd.getResourcePath());
		assertEquals("", spd.getSelectorString());
		assertEquals(0, spd.getSelectors().length);
		assertEquals("", spd.getExtension());
		assertEquals("", spd.getSuffix());
	}

	@Test
	public void testTrailingDotWithSuffix() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/some/path./suffix", new MockResourceResolver(
				"/some/path"));
		assertEquals("/some/path", spd.getResourcePath());
		assertEquals("", spd.getSelectorString());
		assertEquals(0, spd.getSelectors().length);
		assertEquals("", spd.getExtension());
		assertEquals("/suffix", spd.getSuffix());
	}

	@Test
	public void testTrailingDotDot() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/some/path..", new MockResourceResolver(
				"/some/path"));
		assertEquals("/some/path", spd.getResourcePath());
		assertEquals("", spd.getSelectorString());
		assertEquals(0, spd.getSelectors().length);
		assertEquals("", spd.getExtension());
		assertEquals("", spd.getSuffix());
	}

	@Test
	public void testTrailingDotDotWithSuffix() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/some/path../suffix", new MockResourceResolver(
				"/some/path"));
		assertEquals("/some/path", spd.getResourcePath());
		assertEquals("", spd.getSelectorString());
		assertEquals(0, spd.getSelectors().length);
		assertEquals("", spd.getExtension());
		assertEquals("/suffix", spd.getSuffix());
	}

	@Test
	public void testTrailingDotDotDot() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/some/path...", new MockResourceResolver(
				"/some/path"));
		assertEquals("/some/path", spd.getResourcePath());
		assertEquals("", spd.getSelectorString());
		assertEquals(0, spd.getSelectors().length);
		assertEquals("", spd.getExtension());
		assertEquals("", spd.getSuffix());
	}

	@Test
	public void testTrailingDotDotDotWithSuffix() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/some/path.../suffix", new MockResourceResolver(
				"/some/path"));
		assertEquals("/some/path", spd.getResourcePath());
		assertEquals("", spd.getSelectorString());
		assertEquals(0, spd.getSelectors().length);
		assertEquals("", spd.getExtension());
		assertEquals("/suffix", spd.getSuffix());
	}

	@Test
	public void testAllOptions() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/some/path.print.a4.html/some/suffix",
				new MockResourceResolver("/some/path"));
		assertEquals("/some/path", spd.getResourcePath());
		assertEquals("print.a4", spd.getSelectorString());
		assertEquals(2, spd.getSelectors().length);
		assertEquals("print", spd.getSelectors()[0]);
		assertEquals("a4", spd.getSelectors()[1]);
		assertEquals("html", spd.getExtension());
		assertEquals("/some/suffix", spd.getSuffix());
	}

	@Test
	public void testAllEmpty() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/", new MockResourceResolver("/"));
		assertEquals("/", spd.getResourcePath());
		assertEquals("", spd.getSelectorString());
		assertEquals(0, spd.getSelectors().length);
		assertEquals("", spd.getExtension());
		assertEquals("", spd.getSuffix());
	}

	@Test
	public void testPathOnly() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/some/path/here", new MockResourceResolver(
				"/some/path"));
		assertEquals("/some/path/here", spd.getResourcePath());
		assertEquals("", spd.getSelectorString());
		assertEquals(0, spd.getSelectors().length);
		assertEquals("", spd.getExtension());
		assertEquals("", spd.getSuffix());
	}

	@Test
	public void testPathWithExtensionOnly() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/some/path/here.html", new MockResourceResolver(
				"/some/path/here.html"));
		assertEquals("/some/path/here.html", spd.getResource().getPath());
		assertEquals("", spd.getSelectorString());
		assertEquals(0, spd.getSelectors().length);
		assertEquals("", spd.getExtension());
		assertEquals("", spd.getSuffix());
	}

	@Test
	public void testPathAndExtensionOnly() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/some/path/here.html", new MockResourceResolver(
				"/some/path"));
		assertEquals("/some/path/here", spd.getResourcePath());
		assertEquals("", spd.getSelectorString());
		assertEquals(0, spd.getSelectors().length);
		assertEquals("html", spd.getExtension());
		assertEquals("", spd.getSuffix());
	}

	@Test
	public void testPathAndOneSelectorOnly() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/some/path/here.print.html",
				new MockResourceResolver("/some/path"));
		assertEquals("/some/path/here", spd.getResourcePath());
		assertEquals("print", spd.getSelectorString());
		assertEquals(1, spd.getSelectors().length);
		assertEquals("print", spd.getSelectors()[0]);
		assertEquals("html", spd.getExtension());
		assertEquals("", spd.getSuffix());
	}

	@Test
	public void testPathExtAndSuffix() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/some/path/here.html/something",
				new MockResourceResolver("/some/path"));
		assertEquals("/some/path/here", spd.getResourcePath());
		assertEquals("", spd.getSelectorString());
		assertEquals(0, spd.getSelectors().length);
		assertEquals("html", spd.getExtension());
		assertEquals("/something", spd.getSuffix());
	}

	@Test
	public void testSelectorsSplit() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/some/path.print.a4.html/some/suffix",
				new MockResourceResolver("/some/path"));
		assertEquals("/some/path", spd.getResourcePath());
		assertEquals(2, spd.getSelectors().length);
		assertEquals("print", spd.getSelectors()[0]);
		assertEquals("a4", spd.getSelectors()[1]);
		assertEquals("html", spd.getExtension());
		assertEquals("/some/suffix", spd.getSuffix());
	}

	public void testPartialResolutionB() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/some/path.print.a4.html/some/suffix",
				new MockResourceResolver("/some/path"));
		assertEquals("/some/path", spd.getResource().getPath());
		assertEquals("print.a4", spd.getSelectorString());
		assertEquals(2, spd.getSelectors().length);
		assertEquals("print", spd.getSelectors()[0]);
		assertEquals("a4", spd.getSelectors()[1]);
		assertEquals("html", spd.getExtension());
		assertEquals("/some/suffix", spd.getSuffix());
	}

	public void testPartialResolutionC() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/some/path.print.a4.html/some/suffix",
				new MockResourceResolver("/some/path"));
		assertEquals("/some/path.print", spd.getResource().getPath());
		assertEquals("a4", spd.getSelectorString());
		assertEquals(1, spd.getSelectors().length);
		assertEquals("a4", spd.getSelectors()[0]);
		assertEquals("html", spd.getExtension());
		assertEquals("/some/suffix", spd.getSuffix());
	}

	public void testPartialResolutionD() {
		SlingPathDecomposer spd = new SlingPathDecomposer("/some/path.print.a4.html/some/suffix",
				new MockResourceResolver("/some/path"));
		assertEquals("/some/path.print.a4", spd.getResource().getPath());
		assertEquals("", spd.getSelectorString());
		assertEquals(0, spd.getSelectors().length);
		assertEquals("html", spd.getExtension());
		assertEquals("/some/suffix", spd.getSuffix());
	}

	public void testDotsAroundSuffix() {
		SlingPathDecomposer spd = new SlingPathDecomposer(
				"/libs/foo/content/something/formitems.json/image/vnd/xnd/knd.xml", new MockResourceResolver(
						"/some/path"));
		assertEquals("/libs/foo/content/something/formitems", spd.getResource().getPath());
		assertEquals("json", spd.getExtension());
		assertEquals("", spd.getSelectorString());
		assertEquals("/image/vnd/xnd/knd.xml", spd.getSuffix());
	}

}
