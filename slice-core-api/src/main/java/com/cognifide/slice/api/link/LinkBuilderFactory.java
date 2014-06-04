package com.cognifide.slice.api.link;

import java.net.MalformedURLException;

/*
 * #%L
 * Slice - Core API
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

/**
 * Factory for creating LinkBuilder instances.
 * 
 * @author Rafał Malinowski
 */
public interface LinkBuilderFactory {

	/**
	 * Create new empty LinkBuilder.
	 */
	LinkBuilder getLinkBuilder();

	/**
	 * Create LinkBuilder based on link.
	 */
	LinkBuilder getLinkBuilder(Link link);

	/**
	 * Create LinkBuilder based on URL string passed to constructor. URL decomposition is implemented
	 * according to: http://sling.apache.org/documentation/the-sling-engine/url-decomposition.html which means
	 * that:
	 * <ul>
	 * <li>{@link LinkBuilder#getPath() resource path} - The longest substring of the request URL such that
	 * the resource path is either the complete request URL or the next character in the request URL after the
	 * resource path is a dot (<code>.</code>). The resource path is available as a strings
	 * {@link com.cognifide.slice.api.link.LinkBuilder#getPath()}.
	 * <li>{@link com.cognifide.slice.api.link.LinkBuilder#getSelectors() selectors} - If the first character
	 * in the request URI after the content path is a dot, the string after the dot up to but not including
	 * the last dot before the next slash character or the end of the request URI. If the content path spans
	 * the complete request URI or if a slash follows the content path in the request, then no selectors
	 * exist. If only one dot follows the content path before the end of the request URI or the next slash, no
	 * selectors exist. The selectors are available as a list of strings {@link LinkBuilder#getSelectors()}.
	 * <li>{@link com.cognifide.slice.api.link.LinkBuilder#getExtension() extension} - The string after the
	 * last dot after the content path in the request uri but before the end of the request uri or the next
	 * slash after the content path in the request uri. If the content path spans the complete request URI or
	 * a slash follows the content path in the request URI, the extension is empty. The extension is available
	 * as a strings {@link com.cognifide.slice.api.link.LinkBuilder#getExtension()}.
	 * <li>{@link com.cognifide.slice.api.link.LinkBuilder#getSuffix() suffix path} - If the request URI
	 * contains a slash character after the content path and optional selectors and extension, the path
	 * starting with the slash upto the end of the request URI is the suffix path. The suffix path is
	 * available as a strings {@link LinkBuilder#getSuffix()} ()}.
	 * </ul>
	 */
	LinkBuilder getLinkBuilder(String url) throws MalformedURLException;

}
