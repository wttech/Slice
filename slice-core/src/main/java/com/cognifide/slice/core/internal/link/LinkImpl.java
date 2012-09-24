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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.request.RequestPathInfo;

import com.cognifide.slice.api.link.Link;

public class LinkImpl implements Link {

	private static final Pattern DOT_PATTERN = Pattern.compile("\\.");

	private final String path;

	private final String protocol;

	private final String domain;

	private final List<String> selectors; // unmodifiable list

	private final String extension;

	private final String suffix;

	private final String queryString;

	private final String fragment;

	/**
	 * This constructor is, as of now, used only inside a framework. There is no need to use it outside the
	 * framework, as it can be provided/injected anywhere and then new link can be created.
	 * 
	 * @param path resource path is everything from the start up to the first dot after the last slash
	 * (excluding extension and suffix)
	 * @param selectorsString is the dot-separated elements between the path and the extension
	 * @param extension is everything between the last dot in the URL and the next slash (or the end of the
	 * string)
	 * @param suffix is everything after the extension (including the slash)
	 */
	public LinkImpl(String path, String selectorsString, String extension, String suffix) {
		this(path, selectorsString, extension, suffix, null, null);
	}

	/**
	 * This constructor is, as of now, used only inside a framework. There is no need to use it outside the
	 * framework, as it can be provided/injected anywhere and then new link can be created.
	 * 
	 * @param path resource path is everything from the start up to the first dot after the last slash
	 * (excluding extension and suffix)
	 * @param selectorsString is the dot-separated elements between the path and the extension
	 * @param extension is everything between the last dot in the URL and the next slash (or the end of the
	 * string)
	 * @param suffix is everything after the extension (including the slash)
	 */
	public LinkImpl(String path, String selectorsString, String extension, String suffix, String queryString,
			String fragment) {
		this.domain = "";
		this.protocol = "";
		this.path = path;
		this.extension = extension;
		this.suffix = suffix;
		this.queryString = queryString;
		this.fragment = fragment;
		if (StringUtils.isBlank(selectorsString)) {
			selectors = Collections.<String> emptyList();
		} else {
			selectors = Collections.unmodifiableList(Arrays.asList(DOT_PATTERN.split(selectorsString)));
		}
	}

	/**
	 * Creates new instance of the Link by passing the internal state. Use with caution - whenever you pass
	 * {{selectors}} list make sure it's unmodifiable.
	 * 
	 * @param path resource path is everything from the start up to the first dot after the last slash
	 * (excluding extension and suffix)
	 * @param selectors list of selectors, cannot be null, should be unmodifiable
	 * @param extension is everything between the last dot in the URL and the next slash (or the end of the
	 * string)
	 * @param suffix is everything after the extension (including the slash)
	 */
	public LinkImpl(String path, List<String> selectors, String extension, String suffix, String queryString,
			String fragment) {
		this("", "", path, selectors, extension, suffix, queryString, fragment);
	}

	public LinkImpl(String protocol, String domain, String path, List<String> selectors, String extension,
			String suffix, String queryString, String fragment) {
		this.protocol = protocol;
		this.domain = domain;
		this.path = path;
		this.selectors = selectors;
		this.extension = extension;
		this.suffix = suffix;
		this.queryString = queryString;
		this.fragment = fragment;
	}

	public LinkImpl(RequestPathInfo pathInfo) {
		this.path = pathInfo.getResourcePath();
		String[] selectorsArray = pathInfo.getSelectors();
		this.selectors = Collections.unmodifiableList(Arrays.asList(selectorsArray));
		this.extension = pathInfo.getExtension();
		this.suffix = pathInfo.getSuffix();
		this.fragment = "";
		this.queryString = "";
		this.domain = "";
		this.protocol = "";
	}

	/**
	 * Creates string representation of this link.
	 * 
	 * @return string representation like this: /resource/path.selector.ext/suffix?queryParam=value#fragment
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		appendProtocolAndDomain(result);
		appendPath(result);
		appendSelectors(result);
		appendExtension(result);
		appendSuffix(result);
		appendQueryString(result);
		appendFragment(result);
		return result.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cognifide.slice.commons.util.link.Link#getExtension()
	 */
	@Override
	public String getExtension() {
		return extension;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cognifide.slice.commons.util.link.Link#getPath()
	 */
	@Override
	public String getPath() {
		return path;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cognifide.slice.commons.util.link.Link#getSelectors()
	 */
	@Override
	public List<String> getSelectors() {
		return selectors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cognifide.slice.commons.util.link.Link#getSelectorsString()
	 */
	@Override
	public String getSelectorsString() {
		if (selectors.isEmpty()) {
			// this is not how does the CQ's PathInfo works, but returning null does not seem to be a good
			// idea
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for (String selector : selectors) {
			builder.append(selector);
			builder.append(".");
		}
		int lastindex = builder.length() - 1;
		return builder.substring(0, lastindex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cognifide.slice.commons.util.link.Link#getSuffix()
	 */
	@Override
	public String getSuffix() {
		return suffix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cognifide.slice.commons.util.link.Link#getQueryString()
	 */
	@Override
	public String getQueryString() {
		return queryString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cognifide.slice.commons.util.link.Link#getfragment()
	 */
	@Override
	public String getFragment() {
		return fragment;
	}

	@Override
	public String getDomain() {
		return domain;
	}

	@Override
	public String getProtocol() {
		return protocol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cognifide.slice.commons.util.link.Link#hasSuffix()
	 */
	@Override
	public boolean hasSuffix() {
		return StringUtils.isNotBlank(suffix);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cognifide.slice.commons.util.link.Link#containsSelector(java.util.regex.Pattern)
	 */
	@Override
	public boolean containsSelector(Pattern selectorPattern) {
		if (selectorPattern != null) {
			for (String s : selectors) {
				if (selectorPattern.matcher(s).matches()) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cognifide.slice.commons.util.link.Link#containsSelector(java.lang.String)
	 */
	@Override
	public boolean containsSelector(String selector) {
		if (StringUtils.isNotBlank(selector)) {
			for (String s : selectors) {
				if (StringUtils.equals(selector, s)) {
					return true;
				}
			}
		}
		return false;
	}

	private void appendProtocolAndDomain(StringBuilder result) {
		if (StringUtils.isNotBlank(protocol)) {
			result.append(protocol);
			if (!StringUtils.endsWith(protocol, "://")) {
				result.append("://");
			}
		}
		if (StringUtils.isNotBlank(domain)) {
			result.append(domain);
		}
	}

	private void appendPath(StringBuilder result) {
		result.append(StringUtils.defaultString(path));
	}

	private void appendSelectors(StringBuilder result) {
		for (String selector : selectors) {
			result.append(".");
			result.append(selector);
		}
	}

	private void appendExtension(StringBuilder result) {
		if (StringUtils.isNotBlank(extension)) {
			result.append(".");
			result.append(extension);
		}
	}

	private void appendSuffix(StringBuilder result) {
		if (hasSuffix()) {
			if (suffix.charAt(0) != '/') {
				result.append("/");
			}
			result.append(suffix);
		}
	}

	private void appendQueryString(StringBuilder result) {
		if (StringUtils.isNotBlank(queryString)) {
			if (queryString.charAt(0) != '?') {
				result.append("?");
			}
			result.append(queryString);
		}
	}

	private void appendFragment(StringBuilder result) {
		if (StringUtils.isNotBlank(fragment)) {
			if (fragment.charAt(0) != '#') {
				result.append("#");
			}
			result.append(fragment);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((extension == null) ? 0 : extension.hashCode());
		result = prime * result + ((fragment == null) ? 0 : fragment.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
		result = prime * result + ((queryString == null) ? 0 : queryString.hashCode());
		result = prime * result + ((selectors == null) ? 0 : selectors.hashCode());
		result = prime * result + ((suffix == null) ? 0 : suffix.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		final LinkImpl other = (LinkImpl) obj;
		if (!StringUtils.equals(domain, other.domain)) {
			return false;
		}
		if (!StringUtils.equals(extension, other.extension)) {
			return false;
		}
		if (!StringUtils.equals(fragment, other.fragment)) {
			return false;
		}
		if (!StringUtils.equals(path, other.path)) {
			return false;
		}
		if (!StringUtils.equals(protocol, other.protocol)) {
			return false;
		}
		if (!StringUtils.equals(queryString, other.queryString)) {
			return false;
		}
		if (!StringUtils.equals(getSelectorsString(), other.getSelectorsString())) {
			return false;
		}
		if (!StringUtils.equals(suffix, other.suffix)) {
			return false;
		}

		return true;
	}
}
