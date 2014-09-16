/*-
 * #%L
 * Slice - Mapper
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

package com.cognifide.slice.mapper.helper;


import org.apache.commons.lang.StringUtils;

/**
 * Contains methods for escaping string, like {@link StringEscapeUtils}, but with regard to e.g. UTF-8 encoded
 * HTMLs.
 * 
 * @author Albert Cenkier
 * @author Jan Kuźniak
 * @author Krystian Nowak
 * 
 */
public final class Utf8StringEscapeUtils {

	private static final String[] SEARCH_LIST = new String[] { "&", "\"", "\'", "<", ">" };

	private static final String[] REPLACEMENT_LIST = new String[] { "&amp;", "&quot;", "&#39;", "&lt;",
			"&gt;" };

	private Utf8StringEscapeUtils() {
		// hiding constructor for an utility class
	}

	/**
	 * Escapes HTML encoded in UTF-8 range, as opposed to {@link StringEscapeUtils#escapeHtml(String)} that
	 * escapes everything outside ASCII range. This method is more like
	 * {@link StringEscapeUtils#escapeXml(String)}, but the <tt>&apos;</tt> entity is not used because it's
	 * not a legal HTML entity.
	 * 
	 * @param input the <tt>String</tt> to escape, may be null
	 * @return a new escaped <tt>String</tt>, <tt>null</tt> if null string input.
	 */
	public static String escapeUtf8Html(String input) {
		if (null == input) {
			return null;
		}
		return StringUtils.replaceEach(input, SEARCH_LIST, REPLACEMENT_LIST);
	}
}
