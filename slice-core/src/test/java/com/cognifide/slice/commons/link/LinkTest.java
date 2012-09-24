package com.cognifide.slice.commons.link;

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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

import com.cognifide.slice.api.link.Link;
import com.cognifide.slice.core.internal.link.LinkImpl;

/**
 * @author Jan Kuźniak
 * @author maciej.majchrzak
 */
public class LinkTest {

	@Test
	public void testEmptyLink() {
		Link link = new LinkImpl(null, (String) null, null, null, null, null);
		assertEquals("", "", link.toString());
		link = new LinkImpl("", "", "", "", "", "");
		assertEquals("", "", link.toString());
		link = new LinkImpl(null, (String) null, null, null);
		assertEquals("", "", link.toString());
		link = new LinkImpl("", "", "", "");
		assertEquals("", "", link.toString());
	}

	@Test
	public void testPathOnlyLink() {
		Link link = new LinkImpl("/content/ab", (String) null, null, null, null, null);
		assertEquals("", "/content/ab", link.toString());
		link = new LinkImpl("/content", "", "", "", "", "");
		assertEquals("", "/content", link.toString());
		link = new LinkImpl("/content/ab", (String) null, null, null);
		assertEquals("", "/content/ab", link.toString());
		link = new LinkImpl("/content", "", "", "");
		assertEquals("", "/content", link.toString());
	}

	@Test
	public void testPathExtensionLink() {
		Link link = new LinkImpl("/content", (String) null, "html", null, null, null);
		assertEquals("", "/content.html", link.toString());
		link = new LinkImpl("/content", "", "html", "", "", "");
		assertEquals("", "/content.html", link.toString());
		link = new LinkImpl("/content", (String) null, "html", null);
		assertEquals("", "/content.html", link.toString());
		link = new LinkImpl("/content", "", "html", "");
		assertEquals("", "/content.html", link.toString());
	}

	@Test
	public void testPathExtensionSelectorLink() {
		Link link = new LinkImpl("/content", "desktop", "html", null, null, null);
		assertEquals("", "/content.desktop.html", link.toString());
		link = new LinkImpl("/content", "desktop", "html", "", "", "");
		assertEquals("", "/content.desktop.html", link.toString());
		link = new LinkImpl("/content", "desktop", "html", null);
		assertEquals("", "/content.desktop.html", link.toString());
		link = new LinkImpl("/content", "desktop", "html", "");
		assertEquals("", "/content.desktop.html", link.toString());
	}

	@Test
	public void testPathExtensionSelectorsLink() {
		Link link = new LinkImpl("/content", "search.desktop", "html", null, null, null);
		assertEquals("", "/content.search.desktop.html", link.toString());
		link = new LinkImpl("/content", "search.desktop", "html", "", "", "");
		assertEquals("", "/content.search.desktop.html", link.toString());
		link = new LinkImpl("/content", "search.desktop", "html", null);
		assertEquals("", "/content.search.desktop.html", link.toString());
		link = new LinkImpl("/content", "search.desktop", "html", "");
		assertEquals("", "/content.search.desktop.html", link.toString());
	}

	@Test
	public void testPathExtensionSelectorsSuffixLink() {
		Link link = new LinkImpl("/content", "search.desktop", "html", "keyword.html", null, null);
		assertEquals("", "/content.search.desktop.html/keyword.html", link.toString());
		link = new LinkImpl("/content", "search.desktop", "html", "keyword.html", "", "");
		assertEquals("", "/content.search.desktop.html/keyword.html", link.toString());
		link = new LinkImpl("/content", "search.desktop", "html", "keyword.html");
		assertEquals("", "/content.search.desktop.html/keyword.html", link.toString());
	}

	@Test
	public void testPathExtensionQueryLink() {
		Link link = new LinkImpl("/content", (String) null, "html", null, "param1=value1&param2=value2", null);
		assertEquals("", "/content.html?param1=value1&param2=value2", link.toString());
		link = new LinkImpl("/content", "", "html", "", "param1=value1&param2=value2", "");
		assertEquals("", "/content.html?param1=value1&param2=value2", link.toString());
	}

	@Test
	public void testPathExtensionAnchorLink() {
		Link link = new LinkImpl("/content", (String) null, "html", null, null, "paragraph1");
		assertEquals("", "/content.html#paragraph1", link.toString());
		link = new LinkImpl("/content", "", "html", "", "", "paragraph1");
		assertEquals("", "/content.html#paragraph1", link.toString());
	}

	@Test
	public void testPathExtensionQueryAnchorLink() {
		Link link = new LinkImpl("/content", (String) null, "html", null, "param1=value1&param2=value2",
				"paragraph1");
		assertEquals("", "/content.html?param1=value1&param2=value2#paragraph1", link.toString());
		link = new LinkImpl("/content", "", "html", "", "param1=value1&param2=value2", "paragraph1");
		assertEquals("", "/content.html?param1=value1&param2=value2#paragraph1", link.toString());
	}

	@Test
	public void testPathExtensionSelectorQueryLink() {
		Link link = new LinkImpl("/content", "search.desktop", "html", null, "param1=value1&param2=value2",
				null);
		assertEquals("", "/content.search.desktop.html?param1=value1&param2=value2", link.toString());
		link = new LinkImpl("/content", "search.desktop", "html", "", "param1=value1&param2=value2", "");
		assertEquals("", "/content.search.desktop.html?param1=value1&param2=value2", link.toString());
	}

	@Test
	public void testPathExtensionSelectorQueryAnchorLink() {
		Link link = new LinkImpl("/content", "search.desktop", "html", null, "param1=value1&param2=value2",
				"paragraph1");
		assertEquals("", "/content.search.desktop.html?param1=value1&param2=value2#paragraph1",
				link.toString());
		link = new LinkImpl("/content", "search.desktop", "html", "", "param1=value1&param2=value2",
				"paragraph1");
		assertEquals("", "/content.search.desktop.html?param1=value1&param2=value2#paragraph1",
				link.toString());
	}

	@Test
	public void testFullLink() {
		List<String> selectors = new ArrayList<String>();
		selectors.add("search");
		selectors.add("desktop");
		Link link = new LinkImpl("http", "cognifide.com", "/content", selectors, "html", "keyword.html",
				"param1=value1&param2=value2", "paragraph1");
		assertEquals(
				"",
				"http://cognifide.com/content.search.desktop.html/keyword.html?param1=value1&param2=value2#paragraph1",
				link.toString());
		link = new LinkImpl("http://", "cognifide.com", "/content", selectors, "html", "keyword.html",
				"param1=value1&param2=value2", "paragraph1");
		assertEquals(
				"",
				"http://cognifide.com/content.search.desktop.html/keyword.html?param1=value1&param2=value2#paragraph1",
				link.toString());
		link = new LinkImpl("/content", "search.desktop", "html", "keyword.html",
				"param1=value1&param2=value2", "paragraph1");
		assertEquals("", "/content.search.desktop.html/keyword.html?param1=value1&param2=value2#paragraph1",
				link.toString());
	}

	@Test
	public void testContainsSelector() {
		Link link = new LinkImpl("/content", "search.desktop.lightbox", "html", "keyword.html",
				"param1=value1&param2=value2", "paragraph1");
		assertEquals("", true, link.containsSelector("search"));
		assertEquals("", true, link.containsSelector("desktop"));
		assertEquals("", true, link.containsSelector("lightbox"));
		assertEquals("", false, link.containsSelector("lightbox1"));
		assertEquals("", false, link.containsSelector(".lightbox"));
		assertEquals("", false, link.containsSelector("DESKTOP"));
	}

	@Test
	public void testContainsSelectorByPattern() {
		Link link = new LinkImpl("/content", "search.desktop.test10.test23.lightbox", "html", "keyword.html",
				"param1=value1&param2=value2", "paragraph1");
		assertEquals("", true, link.containsSelector(Pattern.compile("test[0-9]+")));
		assertEquals("", true, link.containsSelector(Pattern.compile("lightbox")));
		assertEquals("", false, link.containsSelector(Pattern.compile("test[0-9]\\.")));
		assertEquals("", false, link.containsSelector(Pattern.compile(".test[0-9]+")));
	}

	@Test
	public void testGetSelectorString() {
		Link link = new LinkImpl("/content", "search.desktop", "html", "keyword.html",
				"param1=value1&param2=value2", "paragraph1");
		assertEquals("", "search.desktop", link.getSelectorsString());
		List<String> selectors = new ArrayList<String>();
		selectors.add("search");
		selectors.add("desktop");
		link = new LinkImpl("/content", selectors, "html", "keyword.html", "param1=value1&param2=value2",
				"paragraph1");
		assertEquals("", "search.desktop", link.getSelectorsString());
	}

}
