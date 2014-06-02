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

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

public class LinkDecomposer {

	private final static String[] NO_SELECTORS = new String[0];

	private final ResourceResolver resourceResolver;

	private final String url;

	private String[] selectors;

	private Resource resource;

	private String suffix;

	private String selectorString;

	private String extension;

	private String resourcePath;

	public LinkDecomposer(final String url, final ResourceResolver resourceResolver) {
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
		if (this.resourceResolver.getResource(url) != null) {
			this.resource = this.resourceResolver.getResource(url);
			this.resourcePath = this.resource.getPath();
		} else {
			// check if substring till first dot is a valid resource path
			this.resourcePath = StringUtils.substringBefore(url, ".");
			if (this.resourceResolver.getResource(resourcePath) != null) {
				this.resource = this.resourceResolver.getResource(resourcePath);
			}
		}
		final String pathTail = StringUtils.substringAfter(url, this.resourcePath);

		int firstSlash = pathTail.indexOf('/');
		String pathToSplit;
		if (firstSlash < 0) {
			pathToSplit = pathTail;
			suffix = null;
		} else {
			pathToSplit = pathTail.substring(0, firstSlash);
			suffix = pathTail.substring(firstSlash);
		}

		int lastDot = pathToSplit.lastIndexOf('.');

		if (lastDot <= 1) {
			// no selectors if only extension exists or selectors is empty
			selectorString = null;
			selectors = NO_SELECTORS;
		} else {
			// no selectors if splitting would give an empty array
			String tmpSel = pathToSplit.substring(1, lastDot);
			selectors = tmpSel.split("\\.");
			selectorString = (selectors.length > 0) ? tmpSel : null;
		}

		// extension only if lastDot is not trailing
		extension = (lastDot + 1 < pathToSplit.length()) ? pathToSplit.substring(lastDot + 1) : null;
	}
}
