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


import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;

import com.cognifide.slice.api.scope.ContextScoped;
import com.cognifide.slice.cq.PageChildrenProvider;
import com.cognifide.slice.cq.impl.PageChildrenProviderImpl;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMMode;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public final class CQModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PageChildrenProvider.class).to(PageChildrenProviderImpl.class);
	}

	@Provides
	@ContextScoped
	public PageManager getPageManager(final ResourceResolver resourceResolver) {
		return resourceResolver.adaptTo(PageManager.class);
	}

	@Provides
	// this is NOT supposed to be request scoped, wcm mode can change many times
	// during request
	public WCMMode getWCMMode(final SlingHttpServletRequest request) {
		return WCMMode.fromRequest(request);
	}

}
