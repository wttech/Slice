package com.cognifide.slice.core.internal.module;

/*
 * #%L Slice - Core $Id:$ $HeadURL:$ %% Copyright (C) 2012 Cognifide Limited %% Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License. #L%
 */

import com.cognifide.slice.api.link.LinkExternalizer;
import com.cognifide.slice.api.link.PathMapper;
import com.cognifide.slice.commons.link.LinkExternalizerImpl;
import com.cognifide.slice.core.internal.link.ResourceResolverPathMapper;
import com.google.inject.AbstractModule;

public final class LinkModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(LinkExternalizer.class).to(LinkExternalizerImpl.class);
		bind(PathMapper.class).to(ResourceResolverPathMapper.class);
	}

}
