package com.cognifide.slice.api.link;

/*-
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

import java.util.List;
import java.util.regex.Pattern;

/**
 * Represents an HTML link extended by Sling additions like selectors and suffix. The structure looks like
 * this:<br>
 * <code>protocol://domain/resource/path.selector.ext/suffix?query=string#fragment</code><br>
 * <br>
 * The rules are (following the Sling convention):
 * <ul>
 * <li><b>protocol</b> is everything from the beginning up to "://"</li>,
 * <li><b>domain</b> is everything after "://" and before next slash</li>,
 * <li><b>path</b> is everything after domain (or from start up if domain not present) to the first dot after
 * the last slash before extension and suffix,</li>
 * <li><b>selectors</b> are the dot-separated elements between the path and the extension,</li>
 * <li><b>extension</b> is everything between the last dot in the URL and the next slash (or the end of the
 * string),</li>
 * <li><b>suffix</b> is everything after the extension (including the slash) up to the question mark (or the
 * end of the string if question mark not present),</li>
 * <li><b>query string</b> is everything after question mark up to hash sign (or the end of the string if hash
 * sign not present),</li>
 * <li><b>fragment</b> is everything after hash sign up to the end of string.</li>
 * </ul>
 * 
 * @deprecated It will be removed (along with whole Link API) in next major version - custom solution required
 * 
 * @author Witold Szczerba
 * @author maciej.majchrzak
 */
@Deprecated
public interface Link {

	/**
	 * Returns extension, i.e. everything between the last dot in the URL and the next slash (or the end of
	 * the string)
	 * 
	 * @return extension
	 */
	String getExtension();

	/**
	 * Returns path, i.e. everything after domain (or from start up if domain not present) to the first dot
	 * after the last slash before extension and suffix
	 * 
	 * @return path
	 */
	String getPath();

	/**
	 * Returns all selectors, i.e. the dot-separated elements between the path and the extension
	 * 
	 * @return all selectors
	 */
	List<String> getSelectors();

	/**
	 * @return All the selectors including separating dots or empty string when no selectors.
	 */
	String getSelectorsString();

	/**
	 * Returns suffix, i.e. everything after the extension (including the slash) up to the question mark (or
	 * the end of the string if question mark not present)
	 * 
	 * @return
	 */
	String getSuffix();

	/**
	 * Returns query string without question mark but with ampersands. As a query string is considered
	 * everything after question mark up to hash sign (or the end of the string if hash sign not present)
	 * 
	 * @return query string
	 */
	String getQueryString();

	/**
	 * Returns fragment, i.e. everything after hash sign up to the end of string
	 * 
	 * @return fragment
	 */
	String getFragment();

	/**
	 * Returns protocol, i.e. everything from the beginning up to "://"
	 * 
	 * @return protocol
	 */
	String getProtocol();

	/**
	 * Returns domain, i.e. everything after "://" and before next slash
	 * 
	 * @return domain
	 */
	String getDomain();

	/**
	 * Indicates if the link has suffix or not
	 * 
	 * @return <code>true</code> if the link contains suffix, <code>false</code> otherwise
	 */
	boolean hasSuffix();

	/**
	 * Indicates if the link contains selector(s) matching specified pattern
	 * 
	 * @param selectorPattern pattern to be matched
	 * @return <code>true</code>if at least one selector match the specified pattern, <code>false</code>
	 * otherwise
	 */
	boolean containsSelector(Pattern selectorPattern);

	/**
	 * Indicates if the link contains a specified selector
	 * 
	 * @param selector selector to be checked
	 * @return <code>true</code>if the link contains specified selector, <code>false</code> otherwise
	 */
	boolean containsSelector(String selector);

}