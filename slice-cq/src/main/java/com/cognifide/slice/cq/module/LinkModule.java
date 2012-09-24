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


import org.apache.sling.api.request.RequestPathInfo;

import com.cognifide.slice.api.link.Link;
import com.cognifide.slice.api.link.LinkBuilderFactory;
import com.cognifide.slice.cq.qualifier.RequestedLink;
import com.cognifide.slice.cq.qualifier.RequestedPagePath;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public final class LinkModule extends AbstractModule {

	@Override
	protected void configure() {
	}

	@Provides
	@RequestedLink
	public Link getRequestedPageLink(@RequestedPagePath final String path, final RequestPathInfo rpi,
			final LinkBuilderFactory linkBuilderFactory) {
		return linkBuilderFactory.getLinkBuilder().setPath(path).setExtension(rpi.getExtension())
				.setSelectorString(rpi.getSelectorString()).setSuffix(rpi.getSuffix()).toLink();
	}

}
