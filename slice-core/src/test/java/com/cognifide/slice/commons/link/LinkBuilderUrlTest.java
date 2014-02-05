package com.cognifide.slice.commons.link;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cognifide.slice.api.link.LinkBuilder;

@RunWith(MockitoJUnitRunner.class)
public class LinkBuilderUrlTest {

	@Mock
	private ResourceResolver resourceResolver;

	@Mock
	private Resource resource;

	/*
	 * http://sling.apache.org/apidocs/sling5/org/apache/sling/api/request/RequestPathInfo.html#decomp
	 * describes proper Sling url decompositon.
	 */
	@Test
	public void shouldParseLongSuffixUrl() throws MalformedURLException {

		when(resourceResolver.getResource("/a/b")).thenReturn(resource);
		when(resource.getPath()).thenReturn("/a/b");

		// given
		String url = "http://author.example.com/a/b/c/d.s.txt";

		// when
		LinkBuilder lb = new LinkBuilderImpl(url, resourceResolver);

		// then
		assertEquals("/a/b", lb.getPath());
		assertEquals(null, lb.getSelectors());
		assertEquals(null, lb.getExtension());
		assertEquals("/c/d.s.txt", lb.getSuffix());

	}

	@Test
	public void shouldParseLongSuffixUrlWithQueryString() throws MalformedURLException {

		when(resourceResolver.getResource("/a/b")).thenReturn(resource);
		when(resource.getPath()).thenReturn("/a/b");

		// given
		String url = "http://author.example.com/a/b/c/d.s.txt?q=1";

		// when
		LinkBuilder lb = new LinkBuilderImpl(url, resourceResolver);

		// then
		assertEquals("/a/b", lb.getPath());
		assertEquals(null, lb.getSelectors());
		assertEquals(null, lb.getExtension());
		assertEquals("/c/d.s.txt", lb.getSuffix());
		assertEquals("q=1", lb.getQueryString());

	}

	@Test
	public void shouldParseSuffixUrl() throws MalformedURLException {

		when(resourceResolver.getResource("/a/b")).thenReturn(resource);
		when(resource.getPath()).thenReturn("/a/b");

		// given

		List<String> selectors = new ArrayList<String>();
		selectors.add("s1");

		String url = "http://author.example.com/a/b.s1.html/c/d.s.txt";

		// when
		LinkBuilder lb = new LinkBuilderImpl(url, resourceResolver);

		// then
		assertEquals("/a/b", lb.getPath());
		assertEquals(selectors, lb.getSelectors());
		assertEquals("html", lb.getExtension());
		assertEquals("/c/d.s.txt", lb.getSuffix());

	}

	@Test
	public void shouldParseUrlWithSuffix() throws MalformedURLException {
		when(resourceResolver.getResource(Mockito.anyString())).thenReturn(resource);

		// given
		ArrayList<String> selectors = new ArrayList<String>();
		String url = "http://author.example.com/content/demo/home.json/richtext";

		// when
		LinkBuilderImpl lb = new LinkBuilderImpl(url, resourceResolver);

		// then
		assertEquals("json", lb.getExtension());
		assertEquals("", lb.getFragment());
		assertEquals("/content/demo/home", lb.getPath());
		assertEquals("", lb.getQueryString());
		assertEquals(selectors, lb.getSelectors());
		assertEquals("/richtext", lb.getSuffix());
	}

	@Test()
	public void shouldParseUrlWithComplexSuffixAndFragment() throws MalformedURLException {
		when(resourceResolver.getResource(Mockito.anyString())).thenReturn(resource);

		// given
		ArrayList<String> selectors = new ArrayList<String>();
		selectors.add("s1");
		selectors.add("s2");
		String url = "http://localhost:5602/a/b.s1.s2.html/c/d.s.txt#GOODBYE";

		// when
		LinkBuilderImpl lb = new LinkBuilderImpl(url, resourceResolver);

		// then
		assertEquals("html", lb.getExtension());
		assertEquals("GOODBYE", lb.getFragment());
		assertEquals("/a/b", lb.getPath());
		assertEquals("", lb.getQueryString());
		assertEquals(selectors, lb.getSelectors());
		assertEquals("/c/d.s.txt", lb.getSuffix());
	}

	@Test
	public void shouldParseUrlWithNumericExtensionAndSuffix() throws MalformedURLException {
		when(resourceResolver.getResource(Mockito.anyString())).thenReturn(resource);

		// given
		String url = "http://localhost:5602/a/b.mp3/c";

		// when
		LinkBuilderImpl lb = new LinkBuilderImpl(url, resourceResolver);

		// then
		assertEquals("mp3", lb.getExtension());
		assertEquals("", lb.getFragment());
		assertEquals("/a/b", lb.getPath());
		assertEquals("", lb.getQueryString());
		assertEquals("/c", lb.getSuffix());
	}

	@Test
	public void suffixShouldBeEmptyNotNull() throws MalformedURLException {
		when(resourceResolver.getResource(Mockito.anyString())).thenReturn(resource);

		assertEquals("", new LinkBuilderImpl("http://localhost:5602/a.html", resourceResolver).getSuffix());
		assertEquals("",
				new LinkBuilderImpl("http://localhost:5602/a.selector.html", resourceResolver).getSuffix());
	}

	@Test
	public void suffixCanHaveMultipleDots() throws MalformedURLException {
		when(resourceResolver.getResource(Mockito.anyString())).thenReturn(resource);

		assertEquals("/b.a/c.d",
				new LinkBuilderImpl("http://localhost:5602/a.html/b.a/c.d", resourceResolver).getSuffix());
	}

	@Test(expected = MalformedURLException.class)
	public void shouldThrowMalformedUrlException() throws MalformedURLException {
		// given
		String malformedUrl = "lorem ipsum";
		new LinkBuilderImpl(malformedUrl, resourceResolver);
	}
}
