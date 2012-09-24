package com.cognifide.slice.cq.module;

/*
 * #%L
 * Slice - CQ Add-on
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


import com.cognifide.slice.api.qualifier.Nullable;
import com.cognifide.slice.api.qualifier.RequestedResourcePath;
import com.cognifide.slice.api.scope.ContextScoped;
import com.cognifide.slice.cq.qualifier.RequestedPage;
import com.cognifide.slice.cq.qualifier.RequestedPagePath;
import com.cognifide.slice.cq.utils.PathUtils;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public final class RequestedPageModule extends AbstractModule {

	@Override
	protected void configure() {
	}

	@Provides
	@ContextScoped
	@RequestedPage
	public Page getRequestedPage(@RequestedPagePath @Nullable final String requestedPagePath,
			final PageManager pageManager) {
		return null == requestedPagePath ? null : pageManager.getPage(requestedPagePath);
	}

	@Provides
	@ContextScoped
	@RequestedPagePath
	public String getRequestedPagePath(@RequestedResourcePath final String requestedResourcePath,
			final PathUtils pathUtils) {
		return pathUtils.getPagePathFromResourcePath(requestedResourcePath);
	}

}
