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

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

public class SlingPathDecomposer {

	private final static String[] NO_SELECTORS = new String[0];

	private final ResourceResolver resourceResolver;

	private final String url;

	private String[] selectors;

	private Resource resource;

	private String suffix;

	private String selectorString;

	private String extension;

	private String resourcePath;

	public SlingPathDecomposer(final String url, final ResourceResolver resourceResolver) {
		this.url = url;
		this.resourceResolver = resourceResolver;
		decompose(url);
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public String getUrl() {
		return url;
	}

	public Resource getResource() {
		return resource;
	}

	public String[] getSelectors() {
		return selectors;
	}

	public String getSuffix() {
		return suffix;
	}

	public String getSelectorString() {
		return selectorString;
	}

	public String getExtension() {
		return extension;
	}

	private void decompose(String url) {
		// check if whole path (with dot!) is a valid resource path
		if (getResource(url) != null) {
			this.resourcePath = url;
		} else {
			this.resourcePath = StringUtils.substringBefore(url, ".");
		}
		this.resource = getResource(this.resourcePath);
		final String pathTail = StringUtils.substringAfter(url, this.resourcePath);
		final String pathToSplit = extractPathToSplit(pathTail);
		this.suffix = readSuffix(pathTail);
		this.selectors = readSelectors(pathToSplit);
		this.selectorString = readSelectorString(pathToSplit);
		this.extension = readExtension(pathToSplit);
	}

	private Resource getResource(final String path) {
		return this.resourceResolver.getResource(path);
	}

	private String extractPathToSplit(String pathTail) {
		String result;
		int firstSlash = pathTail.indexOf('/');
		if (firstSlash < 0) {
			result = pathTail;
		} else {
			result = pathTail.substring(0, firstSlash);
		}
		return result;
	}

	private String readSuffix(String pathTail) {
		int firstSlash = pathTail.indexOf('/');
		String result = StringUtils.EMPTY;
		if (firstSlash >= 0) {
			result = pathTail.substring(firstSlash);
		}
		return result;
	}

	private String readExtension(String pathToSplit) {
		String result = StringUtils.EMPTY;
		int lastDot = pathToSplit.lastIndexOf('.');
		if (lastDot + 1 < pathToSplit.length()) {
			result = pathToSplit.substring(lastDot + 1);
		}
		return result;
	}

	private String[] readSelectors(final String pathToSplit) {
		String[] result = NO_SELECTORS;
		int lastDot = pathToSplit.lastIndexOf('.');
		if (lastDot > 1) {
			result = pathToSplit.substring(1, lastDot).split("\\.");
		}
		return result;
	}

	private String readSelectorString(final String pathToSplit) {
		int lastDot = pathToSplit.lastIndexOf('.');
		String result = StringUtils.EMPTY;
		if (lastDot > 1) {
			String tmpSel = pathToSplit.substring(1, lastDot);
			if (tmpSel.split("\\.").length > 0) {
				result = tmpSel;
			}
		}
		return result;
	}
}
