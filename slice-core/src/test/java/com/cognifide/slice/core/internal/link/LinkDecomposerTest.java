package com.cognifide.slice.core.internal.link;

/*
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

public class LinkDecomposerTest {

	@Test(expected = NullPointerException.class)
	public void testNullUrl() {
		LinkDecomposer ld = new LinkDecomposer(null, null);
	}

	@Test
	public void testGetResolutionPathInfo() {
		LinkDecomposer ld = new LinkDecomposer("/some/path/here.html/something", new MockResourceResolver(
				"/some/path/here"));
		assertNotNull(ld.getResource());
		assertEquals("/some/path/here", ld.getResourcePath());
		assertArrayEquals(new String[] {}, ld.getSelectors());
		assertEquals("html", ld.getExtension());
		assertEquals("/something", ld.getSuffix());
	}

	@Test
	public void testUrlExistingResourcePathOnly() {
		LinkDecomposer ld = new LinkDecomposer("/a/b", new MockResourceResolver("/a/b"));
		assertNotNull(ld.getResource());
		assertEquals("/a/b", ld.getResourcePath());
		assertEquals("/a/b", ld.getResource().getPath());
		assertArrayEquals(new String[] {}, ld.getSelectors());
		assertNull(ld.getSelectorString());
		assertNull(ld.getExtension());
		assertNull(ld.getSuffix());
	}

	@Test
	public void testUrlNotExistingResourcePathOnly() {
		LinkDecomposer ld = new LinkDecomposer("/a/b/c", new MockResourceResolver("/a/b"));
		assertNull(ld.getResource());
		assertEquals("/a/b/c", ld.getResourcePath());
		assertArrayEquals(new String[] {}, ld.getSelectors());
		assertNull(ld.getSelectorString());
		assertNull(ld.getExtension());
		assertNull(ld.getSuffix());
	}

	@Test
	public void testUrlWithExtension() {
		LinkDecomposer ld = new LinkDecomposer("/a/b.html", new MockResourceResolver("/a/b"));
		assertNotNull(ld.getResource());
		assertEquals("/a/b", ld.getResourcePath());
		assertEquals("/a/b", ld.getResource().getPath());
		assertArrayEquals(new String[] {}, ld.getSelectors());
		assertNull(ld.getSelectorString());
		assertEquals("html", ld.getExtension());
		assertNull(ld.getSuffix());
	}

	@Test
	public void testResourcePathWithSelectorAndExtenstion() {
		LinkDecomposer ld = new LinkDecomposer("/a/b.s1.html", new MockResourceResolver("/a/b"));
		assertNotNull(ld.getResource());
		assertEquals("/a/b", ld.getResourcePath());
		assertEquals("/a/b", ld.getResource().getPath());
		assertEquals(1, ld.getSelectors().length);
		assertEquals("s1", ld.getSelectorString());
		assertEquals("s1", ld.getSelectors()[0]);
		assertEquals("html", ld.getExtension());
		assertNull(ld.getSuffix());
	}

	@Test
	public void testResourcePathWithTwoSelectorAndExtenstion() {
		LinkDecomposer ld = new LinkDecomposer("/a/b.s1.s2.html", new MockResourceResolver("/a/b"));
		assertNotNull(ld.getResource());
		assertEquals("/a/b", ld.getResourcePath());
		assertEquals("/a/b", ld.getResource().getPath());
		assertEquals(2, ld.getSelectors().length);
		assertEquals("s1.s2", ld.getSelectorString());
		assertEquals("s1", ld.getSelectors()[0]);
		assertEquals("s2", ld.getSelectors()[1]);
		assertEquals("html", ld.getExtension());
		assertNull(ld.getSuffix());
	}

	@Test
	public void testResourceExistingWithSuffixOnly() {
		LinkDecomposer ld = new LinkDecomposer("/a/b./c/d", new MockResourceResolver("/a/b"));
		assertNotNull(ld.getResource());
		assertEquals("/a/b", ld.getResourcePath());
		assertEquals(0, ld.getSelectors().length);
		assertNull(ld.getSelectorString());
		assertNull(ld.getExtension());
		assertEquals("/c/d", ld.getSuffix());
	}

	@Test
	public void testResourceExistingWithExtensionWithSuffix() {
		LinkDecomposer ld = new LinkDecomposer("/a/b.html/c/d", new MockResourceResolver("/a/b"));
		assertNotNull(ld.getResource());
		assertEquals("/a/b", ld.getResourcePath());
		assertEquals(0, ld.getSelectors().length);
		assertNull(ld.getSelectorString());
		assertEquals("html", ld.getExtension());
		assertEquals("/c/d", ld.getSuffix());
	}

	@Test
	public void testResourceExistingWithSelectorWithExtensionWithSuffix() {
		LinkDecomposer ld = new LinkDecomposer("/a/b.s1.html/c/d", new MockResourceResolver("/a/b"));
		assertNotNull(ld.getResource());
		assertEquals("/a/b", ld.getResourcePath());
		assertEquals(1, ld.getSelectors().length);
		assertEquals("s1", ld.getSelectors()[0]);
		assertEquals("s1", ld.getSelectorString());
		assertEquals("html", ld.getExtension());
		assertEquals("/c/d", ld.getSuffix());
	}

	@Test
	public void testResourceExistingWithTwoSelectorWithExtensionWithSuffix() {
		LinkDecomposer ld = new LinkDecomposer("/a/b.s1.s2.html/c/d", new MockResourceResolver("/a/b"));
		assertNotNull(ld.getResource());
		assertEquals("/a/b", ld.getResourcePath());
		assertEquals(2, ld.getSelectors().length);
		assertEquals("s1", ld.getSelectors()[0]);
		assertEquals("s2", ld.getSelectors()[1]);
		assertEquals("s1.s2", ld.getSelectorString());
		assertEquals("html", ld.getExtension());
		assertEquals("/c/d", ld.getSuffix());
	}

	@Test
	public void testNotExistingResourceExistingWithTwoSelectorWithExtensionWithSuffix() {
		LinkDecomposer ld = new LinkDecomposer("/a/b/c/d.s.txt", new MockResourceResolver("/a/b"));
		assertNull(ld.getResource());
		assertEquals("/a/b/c/d", ld.getResourcePath());
		assertEquals(1, ld.getSelectors().length);
		assertEquals("s", ld.getSelectors()[0]);
		assertEquals("s", ld.getSelectorString());
		assertEquals("txt", ld.getExtension());
		assertNull(ld.getSuffix());
	}

	@Test
	public void testExistingResourceExistingWithoutSelectorWithExtensionWithSuffix() {
		LinkDecomposer ld = new LinkDecomposer("/a/b.html/c/d.s.txt", new MockResourceResolver("/a/b"));
		assertNotNull(ld.getResource());
		assertEquals("/a/b", ld.getResource().getPath());
		assertEquals("/a/b", ld.getResourcePath());
		assertEquals(0, ld.getSelectors().length);
		assertNull(ld.getSelectorString());
		assertEquals("html", ld.getExtension());
		assertEquals("/c/d.s.txt", ld.getSuffix());
	}

	@Test
	public void testExistingResourceExistingWithoutSelectorWithExtensionWithSuffix2() {
		LinkDecomposer ld = new LinkDecomposer("/a/b.s1.html/c/d.s.txt", new MockResourceResolver("/a/b"));
		assertNotNull(ld.getResource());
		assertEquals("/a/b", ld.getResource().getPath());
		assertEquals("/a/b", ld.getResourcePath());
		assertEquals(1, ld.getSelectors().length);
		assertEquals("s1", ld.getSelectors()[0]);
		assertEquals("s1", ld.getSelectorString());
		assertEquals("html", ld.getExtension());
		assertEquals("/c/d.s.txt", ld.getSuffix());
	}

	@Test
	public void testExistingResourceExistingWithoutSelectorWithExtensionWithSuffix3() {
		LinkDecomposer ld = new LinkDecomposer("/a/b.s1.s2.html/c/d.s.txt", new MockResourceResolver("/a/b"));
		assertNotNull(ld.getResource());
		assertEquals("/a/b", ld.getResource().getPath());
		assertEquals("/a/b", ld.getResourcePath());
		assertEquals(2, ld.getSelectors().length);
		assertEquals("s1", ld.getSelectors()[0]);
		assertEquals("s2", ld.getSelectors()[1]);
		assertEquals("s1.s2", ld.getSelectorString());
		assertEquals("html", ld.getExtension());
		assertEquals("/c/d.s.txt", ld.getSuffix());
	}

	// tests taken from Sling:

	@Test
	public void testTrailingDot() {
		LinkDecomposer ld = new LinkDecomposer("/some/path.", new MockResourceResolver("/some/path"));
		assertEquals("/some/path", ld.getResourcePath());
		assertNull("Selectors are null", ld.getSelectorString());
		assertEquals(0, ld.getSelectors().length);
		assertNull("Extension is null", ld.getExtension());
		assertNull("Suffix is null", ld.getSuffix());
	}

	@Test
	public void testTrailingDotWithSuffix() {
		LinkDecomposer ld = new LinkDecomposer("/some/path./suffix", new MockResourceResolver("/some/path"));
		assertEquals("/some/path", ld.getResourcePath());
		assertNull("Selectors are null", ld.getSelectorString());
		assertEquals(0, ld.getSelectors().length);
		assertNull("Extension is null", ld.getExtension());
		assertEquals("/suffix", ld.getSuffix());
	}

	@Test
	public void testTrailingDotDot() {
		LinkDecomposer ld = new LinkDecomposer("/some/path..", new MockResourceResolver("/some/path"));
		assertEquals("/some/path", ld.getResourcePath());
		assertNull("Selectors are null", ld.getSelectorString());
		assertEquals(0, ld.getSelectors().length);
		assertNull("Extension is null", ld.getExtension());
		assertNull("Suffix is null", ld.getSuffix());
	}

	@Test
	public void testTrailingDotDotWithSuffix() {
		LinkDecomposer ld = new LinkDecomposer("/some/path../suffix", new MockResourceResolver("/some/path"));
		assertEquals("/some/path", ld.getResourcePath());
		assertNull("Selectors are null", ld.getSelectorString());
		assertEquals(0, ld.getSelectors().length);
		assertNull("Extension is null", ld.getExtension());
		assertEquals("/suffix", ld.getSuffix());
	}

	@Test
	public void testTrailingDotDotDot() {
		LinkDecomposer ld = new LinkDecomposer("/some/path...", new MockResourceResolver("/some/path"));
		assertEquals("/some/path", ld.getResourcePath());
		assertNull("Selectors are null", ld.getSelectorString());
		assertEquals(0, ld.getSelectors().length);
		assertNull("Extension is null", ld.getExtension());
		assertNull("Suffix is null", ld.getSuffix());
	}

	@Test
	public void testTrailingDotDotDotWithSuffix() {
		LinkDecomposer ld = new LinkDecomposer("/some/path.../suffix", new MockResourceResolver("/some/path"));
		assertEquals("/some/path", ld.getResourcePath());
		assertNull("Selectors are null", ld.getSelectorString());
		assertEquals(0, ld.getSelectors().length);
		assertNull("Extension is null", ld.getExtension());
		assertEquals("/suffix", ld.getSuffix());
	}

	@Test
	public void testAllOptions() {
		LinkDecomposer ld = new LinkDecomposer("/some/path.print.a4.html/some/suffix",
				new MockResourceResolver("/some/path"));
		assertEquals("/some/path", ld.getResourcePath());
		assertEquals("print.a4", ld.getSelectorString());
		assertEquals(2, ld.getSelectors().length);
		assertEquals("print", ld.getSelectors()[0]);
		assertEquals("a4", ld.getSelectors()[1]);
		assertEquals("html", ld.getExtension());
		assertEquals("/some/suffix", ld.getSuffix());
	}

	@Test
	public void testAllEmpty() {
		LinkDecomposer ld = new LinkDecomposer("/", new MockResourceResolver("/"));
		assertEquals("/", ld.getResourcePath());
		assertNull("Selectors are null", ld.getSelectorString());
		assertEquals(0, ld.getSelectors().length);
		assertNull("Extension is null", ld.getExtension());
		assertNull("Suffix is null", ld.getSuffix());
	}

	@Test
	public void testPathOnly() {
		LinkDecomposer ld = new LinkDecomposer("/some/path/here", new MockResourceResolver("/some/path"));
		assertEquals("/some/path/here", ld.getResourcePath());
		assertNull("Selectors are null", ld.getSelectorString());
		assertEquals(0, ld.getSelectors().length);
		assertNull("Extension is null", ld.getExtension());
		assertNull("Suffix is null", ld.getSuffix());
	}

	@Test
	public void testPathWithExtensionOnly() {
		LinkDecomposer ld = new LinkDecomposer("/some/path/here.html", new MockResourceResolver(
				"/some/path/here.html"));
		assertEquals("/some/path/here.html", ld.getResource().getPath());
		assertNull("Selectors are null", ld.getSelectorString());
		assertEquals(0, ld.getSelectors().length);
		assertNull("Extension is null", ld.getExtension());
		assertNull("Suffix is null", ld.getSuffix());
	}

	@Test
	public void testPathAndExtensionOnly() {
		LinkDecomposer ld = new LinkDecomposer("/some/path/here.html", new MockResourceResolver("/some/path"));
		assertEquals("/some/path/here", ld.getResourcePath());
		assertNull("Selectors are null", ld.getSelectorString());
		assertEquals(0, ld.getSelectors().length);
		assertEquals("html", ld.getExtension());
		assertNull("Suffix is null", ld.getSuffix());
	}

	@Test
	public void testPathAndOneSelectorOnly() {
		LinkDecomposer ld = new LinkDecomposer("/some/path/here.print.html", new MockResourceResolver(
				"/some/path"));
		assertEquals("/some/path/here", ld.getResourcePath());
		assertEquals("print", ld.getSelectorString());
		assertEquals(1, ld.getSelectors().length);
		assertEquals("print", ld.getSelectors()[0]);
		assertEquals("html", ld.getExtension());
		assertNull("Suffix is null", ld.getSuffix());
	}

	@Test
	public void testPathExtAndSuffix() {
		LinkDecomposer ld = new LinkDecomposer("/some/path/here.html/something", new MockResourceResolver(
				"/some/path"));
		assertEquals("/some/path/here", ld.getResourcePath());
		assertNull("Selectors are null", ld.getSelectorString());
		assertEquals(0, ld.getSelectors().length);
		assertEquals("html", ld.getExtension());
		assertEquals("/something", ld.getSuffix());
	}

	@Test
	public void testSelectorsSplit() {
		LinkDecomposer ld = new LinkDecomposer("/some/path.print.a4.html/some/suffix",
				new MockResourceResolver("/some/path"));
		assertEquals("/some/path", ld.getResourcePath());
		assertEquals(2, ld.getSelectors().length);
		assertEquals("print", ld.getSelectors()[0]);
		assertEquals("a4", ld.getSelectors()[1]);
		assertEquals("html", ld.getExtension());
		assertEquals("/some/suffix", ld.getSuffix());
	}

	public void testPartialResolutionB() {
		LinkDecomposer ld = new LinkDecomposer("/some/path.print.a4.html/some/suffix",
				new MockResourceResolver("/some/path"));
		assertEquals("/some/path", ld.getResource().getPath());
		assertEquals("print.a4", ld.getSelectorString());
		assertEquals(2, ld.getSelectors().length);
		assertEquals("print", ld.getSelectors()[0]);
		assertEquals("a4", ld.getSelectors()[1]);
		assertEquals("html", ld.getExtension());
		assertEquals("/some/suffix", ld.getSuffix());
	}

	public void testPartialResolutionC() {
		LinkDecomposer ld = new LinkDecomposer("/some/path.print.a4.html/some/suffix",
				new MockResourceResolver("/some/path"));
		assertEquals("/some/path.print", ld.getResource().getPath());
		assertEquals("a4", ld.getSelectorString());
		assertEquals(1, ld.getSelectors().length);
		assertEquals("a4", ld.getSelectors()[0]);
		assertEquals("html", ld.getExtension());
		assertEquals("/some/suffix", ld.getSuffix());
	}

	public void testPartialResolutionD() {
		LinkDecomposer ld = new LinkDecomposer("/some/path.print.a4.html/some/suffix",
				new MockResourceResolver("/some/path"));
		assertEquals("/some/path.print.a4", ld.getResource().getPath());
		assertNull("Selectors are null", ld.getSelectorString());
		assertEquals(0, ld.getSelectors().length);
		assertEquals("html", ld.getExtension());
		assertEquals("/some/suffix", ld.getSuffix());
	}

	public void testDotsAroundSuffix() {
		LinkDecomposer ld = new LinkDecomposer(
				"/libs/foo/content/something/formitems.json/image/vnd/xnd/knd.xml", new MockResourceResolver(
						"/some/path"));
		assertEquals("/libs/foo/content/something/formitems", ld.getResource().getPath());
		assertEquals("json", ld.getExtension());
		assertNull("Selectors are null", ld.getSelectorString());
		assertEquals("/image/vnd/xnd/knd.xml", ld.getSuffix());
	}

}
