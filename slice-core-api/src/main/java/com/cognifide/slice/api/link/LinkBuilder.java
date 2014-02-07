package com.cognifide.slice.api.link;

/*
 * #%L Slice - Core API $Id:$ $HeadURL:$ %% Copyright (C) 2012 Cognifide Limited %% Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License. #L%
 */

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolver;

/**
 * Allows building links and modifying existing link. Use whenever you need to add/remove selectors, query
 * strings, suffix etc.
 * 
 * @author Jan Kuzniak
 * @author maciej.majchrzak
 */
public interface LinkBuilder {

	/**
	 * Returns;@link LinkImpl} representing data in the builder.
	 * 
	 * @return;@link LinkImpl} representing data in the builder, never null
	 */
	Link toLink();

	/**
	 * Parses a specified url
	 * 
	 * @throws {@link MalformedURLException} when url is not a valid URL.
	 * @param url URL string to be parsed.
	 * @param resourceResolver Resolver used to get the resource's path.
	 * @return this builder
	 */
	LinkBuilder parseUrl(final String url, final ResourceResolver resourceResolver)
			throws MalformedURLException;

	/**
	 * Adds selector (if not blank) as a last one on the list of selectors
	 * 
	 * @param selector selector to be added
	 * @return this builder
	 */
	LinkBuilder addSelector(final String selector);

	/**
	 * Removes selector from the list of selectors if present
	 * 
	 * @param selector selector to be removed from the list of selectors. If blank, nothing happens.
	 * @return this builder
	 */
	LinkBuilder removeSelector(final String selector);

	/**
	 * Removes selector from the list of selectors if matches given regular expression
	 * 
	 * @param selectorRegexp regular expression of the selector to remove from the list. If blank ;@link
	 * IllegalArgumentException} is thrown
	 * @return this builder
	 */
	LinkBuilder removeSelectorRegexp(final String selectorRegexp);

	/**
	 * Retails all selectors which match given regular expression. All selectors which don't match the regular
	 * expression are removed.
	 * 
	 * @param selectorRegexp regular expression of the selector to be retained on the list. If blank ;@link
	 * IllegalArgumentException} is thrown
	 * @return this builder
	 */
	LinkBuilder retainSelectors(final String includeRegexp);

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
	LinkBuilder appendToPath(final String tail);

	/**
	 * Add a new query to path. Please note that multiple values can be stored under the same key. It will be
	 * represented in the following way in final link: "key=value"
	 * 
	 * @param key String under which a value will be stored
	 * @param value value to be stored
	 * @return this builder
	 */
	LinkBuilder addQuery(final String key, final String value);

	/**
	 * Removes a specified value from the list of values stored under specified key for the query string. If
	 * there is no query under key or no specified value, nothing will be removed
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	LinkBuilder removeQuery(final String key, final String value);

	/**
	 * Removes all values stored under specified key for the query string
	 * 
	 * @param key
	 * @return this builder
	 */
	LinkBuilder removeQuery(final String key);

	/**
	 * Returns string representation of query string. It is NOT led by "?" char.
	 * 
	 * @return
	 */
	String getQueryString();

	/**
	 * Sets query string. It must not start with "?", e.g. "param1=value1&param2=value2"
	 * 
	 * @param queryString query string to be saved
	 * @return
	 */
	LinkBuilder setQueryString(String queryString);

	String getPath();

	LinkBuilder setPath(String path);

	String getExtension();

	LinkBuilder setExtension(String extension);

	String getSuffix();

	LinkBuilder setSuffix(String suffix);

	List<String> getSelectors();

	String getFragment();

	LinkBuilder setFragment(String fragment);

	Map<String, List<String>> getQueries();

	LinkBuilder setQueries(Map<String, List<String>> queries);

	String getProtocol();

	LinkBuilder setProtocol(String protocol);

	String getDomain();

	LinkBuilder setDomain(String domain);

	LinkBuilder setSelectors(List<String> selectors);

	LinkBuilder setSelectorString(String selectorString);

}
