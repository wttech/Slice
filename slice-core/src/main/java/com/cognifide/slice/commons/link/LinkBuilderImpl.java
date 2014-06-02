package com.cognifide.slice.commons.link;

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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.cognifide.slice.api.link.Link;
import com.cognifide.slice.api.link.LinkBuilder;
import com.cognifide.slice.core.internal.link.LinkImpl;
import com.cognifide.slice.core.internal.link.SlingPathDecomposer;

/**
 * Allows building links and modifying existing link. Use whenever you need to add/remove selectors, query
 * strings, suffix etc.
 * 
 * @author Jan Kuzniak
 * @author maciej.majchrzak
 * @author adam.zwoniarski
 * @author maciej.rudowicz
 */
public final class LinkBuilderImpl implements LinkBuilder {

	private String path;

	private List<String> selectors;

	private String extension;

	private String suffix;

	private String fragment;

	private Map<String, List<String>> queries;

	private String protocol;

	private String domain;

	/**
	 * Creates an empty builder. All parts of a link are empty.
	 */

	public LinkBuilderImpl() {
		this.path = "";
		this.selectors = new ArrayList<String>();
		this.queries = new HashMap<String, List<String>>();
		this.extension = "";
		this.suffix = "";
		this.fragment = "";
	}

	/**
	 * Creates a builder and sets underlying values using specified link. The values can be modified without
	 * any affection on specified link.
	 * 
	 * @param link
	 */
	public LinkBuilderImpl(final Link link) {
		this.path = link.getPath();
		List<String> linkSelectors = link.getSelectors();
		this.selectors = new ArrayList<String>(linkSelectors.size());
		this.selectors.addAll(linkSelectors);
		this.extension = link.getExtension();
		this.suffix = link.getSuffix();
		setQueryString(link.getQueryString());
		this.fragment = link.getFragment();
	}

	/**
	 * Parses a specified URL string
	 * 
	 * @throws MalformedURLException when url is not a valid URL.
	 * @param url URL string to be parsed.
	 * @param resourceResolver Resolver used to get the resource's path.
	 * @return this builder
	 */
	public LinkBuilderImpl(final String url, final ResourceResolver resourceResolver)
			throws MalformedURLException {
		URL urlHelper = new URL(url);
		SlingPathDecomposer slingPathDecomposer = new SlingPathDecomposer(urlHelper.getPath(),
				resourceResolver);
		this.suffix = slingPathDecomposer.getSuffix();
		this.path = slingPathDecomposer.getResourcePath();
		this.extension = slingPathDecomposer.getExtension();
		this.selectors = Arrays.asList(slingPathDecomposer.getSelectors());
		setQueryString(urlHelper.getQuery());
		this.fragment = (urlHelper.getRef() == null) ? "" : urlHelper.getRef();
		this.protocol = (urlHelper.getProtocol() == null) ? "" : urlHelper.getProtocol();
		this.domain = (urlHelper.getAuthority() == null) ? "" : urlHelper.getAuthority();
	}

	private String resolvePath(final ResourceResolver resourceResolver, String urlPath) {
		String path = StringUtils.substringBefore(urlPath, ".");
		Resource resource = resourceResolver.getResource(path);

		while (null == resource && !StringUtils.isEmpty(path)) {
			path = StringUtils.substringBeforeLast(path, "/");
			resource = resourceResolver.getResource(path);
		}
		return path;
	}

	/**
	 * Returns {@link LinkImpl} representing data in the builder.
	 * 
	 * @return {@link LinkImpl} representing data in the builder, never null
	 */
	@Override
	public Link toLink() {
		return new LinkImpl(protocol, domain, path, Collections.unmodifiableList(selectors), extension,
				suffix, getQueryString(), fragment);
	}

	/**
	 * Same as <code>toLink().toString()</code>
	 */
	@Override
	public String toString() {
		return toLink().toString();
	}

	/**
	 * Adds selector (if not blank) as a last one on the list of selectors
	 * 
	 * @param selector selector to be added
	 * @return this builder
	 */
	@Override
	public LinkBuilder addSelector(final String selector) {
		if (StringUtils.isNotBlank(selector) && !selectors.contains(selector)) {
			selectors.add(selector);
		}
		return this;
	}

	/**
	 * Removes selector from the list of selectors if present
	 * 
	 * @param selector selector to be removed from the list of selectors. If blank, nothing happens.
	 * @return this builder
	 */
	@Override
	public LinkBuilder removeSelector(final String selector) {
		if (StringUtils.isNotBlank(selector)) {
			for (int i = selectors.size() - 1; i >= 0; i--) {
				String s = selectors.get(i);
				if (selector.equals(s)) {
					selectors.remove(i);
				}
			}
		}
		return this;
	}

	/**
	 * Removes selector from the list of selectors if matches given regular expression
	 * 
	 * @param selectorRegexp regular expression of the selector to remove from the list. If blank
	 * {@link IllegalArgumentException} is thrown
	 * @return this builder
	 */
	@Override
	public LinkBuilder removeSelectorRegexp(final String selectorRegexp) {
		if (StringUtils.isBlank(selectorRegexp)) {
			throw new IllegalArgumentException("Selector pattern cannot be blank.");
		}
		Pattern pattern = Pattern.compile(selectorRegexp);

		for (int i = selectors.size() - 1; i >= 0; i--) {
			String s = selectors.get(i);
			if (pattern.matcher(s).matches()) {
				selectors.remove(i);
			}
		}
		return this;
	}

	/**
	 * Retails all selectors which match given regular expression. All selectors which don't match the regular
	 * expression are removed.
	 * 
	 * @param includeRegexp regular expression of the selector to be retained on the list. If blank
	 * {@link IllegalArgumentException} is thrown
	 * @return this builder
	 */
	@Override
	public LinkBuilder retainSelectors(final String includeRegexp) {
		if (StringUtils.isBlank(includeRegexp)) {
			throw new IllegalArgumentException("Selector pattern cannot be blank.");
		}
		Pattern pattern = Pattern.compile(includeRegexp);

		for (int i = selectors.size() - 1; i >= 0; i--) {
			String s = selectors.get(i);
			if (!pattern.matcher(s).matches()) {
				selectors.remove(i);
			}
		}
		return this;
	}

	/**
	 * Concatenates a specified tail with the underlying path using "/" separator. <br>
	 * E.g.<br>
	 * <code>
	 * path="/content", tail="cognifide" => path="/content/cognifide"<br>
	 * path="/content", tail="/cognifide" => path="/content/cognifide"<br>
	 * path="/content", tail="cognifide/" => path="/content/cognifide"<br>
	 * path="/content", tail="/cognifide/" => path="/content/cognifide"<br>
	 * </code>
	 * 
	 * @param tail a fragment to be added at the end of the path. Leading and trailing slashes are removed.
	 * @return this builder
	 */
	@Override
	public LinkBuilder appendToPath(final String tail) {
		String appendTail = StringUtils.removeStart(tail, "/");
		appendTail = StringUtils.removeEnd(appendTail, "/");
		String newPath = path + "/" + appendTail;
		setPath(newPath);
		return this;
	}

	/**
	 * Add a new query to path. Please note that multiple values can be stored under the same key. It will be
	 * represented in the following way in final link: "key=value"
	 * 
	 * @param key String under which a value will be stored
	 * @param value value to be stored
	 * @return this builder
	 */
	@Override
	public LinkBuilder addQuery(final String key, final String value) {
		if (StringUtils.isNotBlank(key)) {
			List<String> list = queries.get(key);
			if (list == null) {
				list = new ArrayList<String>();
				queries.put(key, list);
			}
			if (!list.contains(value)) {
				list.add(value);
			}
		}
		return this;
	}

	/**
	 * Removes a specified value from the list of values stored under specified key for the query string. If
	 * there is no query under key or no specified value, nothing will be removed
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public LinkBuilder removeQuery(final String key, final String value) {
		List<String> list = queries.get(key);
		if (list != null) {
			list.remove(value);
		}
		return this;
	}

	/**
	 * Removes all values stored under specified key for the query string
	 * 
	 * @param key
	 * @return this builder
	 */
	@Override
	public LinkBuilder removeQuery(final String key) {
		queries.remove(key);
		return this;
	}

	/**
	 * Returns string representation of query string. It is NOT led by "?" char.
	 * 
	 * @return
	 */
	@Override
	public String getQueryString() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, List<String>> entry : queries.entrySet()) {
			String key = entry.getKey();
			List<String> values = entry.getValue();
			for (String value : values) {
				sb.append("&").append(key).append('=').append(value);
			}
		}
		String result;
		if (sb.length() > 0) {
			result = sb.substring(1); // remove leading '&'
		} else {
			result = "";
		}
		return result;
	}

	/**
	 * Sets query string. It must not start with "?", e.g. "param1=value1&param2=value2"
	 * 
	 * @param queryString query string to be saved
	 * @return
	 */
	@Override
	public LinkBuilder setQueryString(String queryString) {
		queries = new HashMap<String, List<String>>();
		if (queryString != null) {
			String[] pairs = StringUtils.split(queryString, "&");
			for (String pair : pairs) {
				String[] keyValue = StringUtils.split(pair, "=");
				if (keyValue != null && keyValue.length == 2) {
					String key = keyValue[0];
					String value = keyValue[1];
					addQuery(key, value);
				}
			}
		}
		return this;
	}

	// /////////////////////////////////////////////////////////////////////////
	// getters and setters
	// ///////////////////////////////////////////////////////////////////////
	@Override
	public String getPath() {
		return path;
	}

	@Override
	public LinkBuilder setPath(String path) {
		this.path = path;
		return this;
	}

	@Override
	public String getExtension() {
		return extension;
	}

	@Override
	public LinkBuilder setExtension(String extension) {
		this.extension = extension;
		return this;
	}

	@Override
	public String getSuffix() {
		return suffix;
	}

	@Override
	public LinkBuilder setSuffix(String suffix) {
		this.suffix = suffix;
		return this;
	}

	@Override
	public List<String> getSelectors() {
		return selectors;
	}

	@Override
	public String getFragment() {
		return fragment;
	}

	@Override
	public LinkBuilder setFragment(String fragment) {
		this.fragment = fragment;
		return this;
	}

	@Override
	public Map<String, List<String>> getQueries() {
		return queries;
	}

	@Override
	public LinkBuilder setQueries(Map<String, List<String>> queries) {
		this.queries = queries;
		return this;
	}

	@Override
	public String getProtocol() {
		return protocol;
	}

	@Override
	public LinkBuilder setProtocol(String protocol) {
		this.protocol = protocol;
		return this;
	}

	@Override
	public String getDomain() {
		return domain;
	}

	@Override
	public LinkBuilder setDomain(String domain) {
		this.domain = domain;
		return this;
	}

	@Override
	public LinkBuilder setSelectors(List<String> selectors) {
		this.selectors = Collections.unmodifiableList(selectors);
		return this;
	}

	@Override
	public LinkBuilder setSelectorString(String selectorString) {
		List<String> result = new ArrayList<String>();
		String[] splitSelectors = StringUtils.split(selectorString, ".");
		if (splitSelectors != null) {
			result = Arrays.asList(splitSelectors);
		}
		return setSelectors(result);
	}

}
