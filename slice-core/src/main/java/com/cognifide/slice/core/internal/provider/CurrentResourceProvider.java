/*-
 * #%L
 * Slice - Core
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2012 - 2014 Cognifide Limited
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

package com.cognifide.slice.core.internal.provider;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.cognifide.slice.api.execution.ExecutionContextStack;
import com.cognifide.slice.api.execution.ExecutionContext;
import com.cognifide.slice.util.ResourceUtil;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class CurrentResourceProvider implements Provider<Resource> {

	private final ExecutionContextStack currentExecutionContext;

	private final ResourceResolver resourceResolver;

	@Inject
	public CurrentResourceProvider(final ExecutionContextStack currentExecutionContext,
			final ResourceResolver resourceResolver) {
		this.currentExecutionContext = currentExecutionContext;
		this.resourceResolver = resourceResolver;
	}

	@Override
	public Resource get() {
		final ExecutionContext executionItem = currentExecutionContext.peek();
		if (executionItem.getResource() != null) {
			return executionItem.getResource();
		} else {
			final Resource resource = resourceResolver.getResource(executionItem.getPath());
			return ResourceUtil.getValidResourceOrNull(resource);
		}
	}

}
