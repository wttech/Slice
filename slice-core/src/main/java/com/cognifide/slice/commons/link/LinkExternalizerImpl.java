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

import org.apache.commons.lang.StringUtils;

import com.cognifide.slice.api.link.Link;
import com.cognifide.slice.api.link.LinkExternalizer;
import com.cognifide.slice.api.link.PathMapper;
import com.cognifide.slice.api.qualifier.Nullable;
import com.google.inject.Inject;

/**
 * 
 * @deprecated It will be removed (along with whole Link API) in next major version - custom solution required
 * 
 */
@Deprecated
public class LinkExternalizerImpl implements LinkExternalizer {

	private static final String CONTENT_HANDLE = "/content";

	private final PathMapper pathMapper;

	@Inject
	public LinkExternalizerImpl(@Nullable final PathMapper pathMapper) {
		this.pathMapper = pathMapper;
	}

	@Override
	public Link externalizeLink(final Link link) {
		if (null == link) {
			return null;
		}

		final LinkBuilderImpl linkBuilder = new LinkBuilderImpl(link);

		if (null != pathMapper) {
			linkBuilder.setPath(pathMapper.mapPath(link.getPath()));
		}

		if (StringUtils.isBlank(linkBuilder.getExtension())) {
			linkBuilder.setExtension("html");
		}

		return linkBuilder.toLink();
	}

	@Override
	public Link externalizeContentLink(final Link link) {
		if (null == link) {
			return null;
		}

		if (link.getPath().isEmpty() || !link.getPath().startsWith(CONTENT_HANDLE)) {
			return link;
		}

		final LinkBuilderImpl linkBuilder = new LinkBuilderImpl(link);

		if (null != pathMapper) {
			linkBuilder.setPath(pathMapper.mapPath(link.getPath()));
		}

		if (StringUtils.isBlank(linkBuilder.getExtension())) {
			linkBuilder.setExtension("html");
		}

		return linkBuilder.toLink();
	}

	@Override
	public String externalizePath(final String path) {
		if (StringUtils.isBlank(path)) {
			return StringUtils.EMPTY;
		}

		final LinkBuilderImpl linkBuilder = new LinkBuilderImpl();
		linkBuilder.setPath(path);
		return externalizeLink(linkBuilder.toLink()).toString();
	}

	@Override
	public String externalizeContentPath(final String path) {
		if (StringUtils.isBlank(path)) {
			return StringUtils.EMPTY;
		}

		final LinkBuilderImpl linkBuilder = new LinkBuilderImpl();
		linkBuilder.setPath(path);
		return externalizeContentLink(linkBuilder.toLink()).toString();
	}

}
