/*-
 * #%L
 * Slice - Core
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

package com.cognifide.slice.core.internal.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.cognifide.slice.api.execution.ExecutionContextStack;
import com.cognifide.slice.api.provider.ChildrenProvider;
import com.cognifide.slice.api.scope.ContextScoped;
import com.google.inject.Inject;

/**
 * @deprecated See {@link com.cognifide.slice.api.provider.ModelProvider} and one of its methods: <li>
 * {@link com.cognifide.slice.api.provider.ModelProvider#getChildModels(Class, String)} <li>
 * {@link com.cognifide.slice.api.provider.ModelProvider#getChildModels(Class, Resource)} <li>
 * {@link com.cognifide.slice.api.provider.ModelProvider#getList(Class, java.util.Iterator)}
 */
@Deprecated
@ContextScoped
public class SliceChildrenProvider implements ChildrenProvider {

	private final ResourceResolver resourceResolver;

	private final ExecutionContextStack currentExecutionContext;

	@Inject
	public SliceChildrenProvider(final ResourceResolver resourceResolver,
			final ExecutionContextStack currentExecutionContext) {
		this.resourceResolver = resourceResolver;
		this.currentExecutionContext = currentExecutionContext;
	}

	@Override
	public List<String> getChildren(final String path) {
		ArrayList<String> result = new ArrayList<String>();
		List<Resource> childResources = getChildResources(path);
		for (Resource resource : childResources) {
			result.add(resource.getPath());
		}
		return result;
	}

	@Override
	public List<Resource> getChildResources(final String path) {
		ArrayList<Resource> result = new ArrayList<Resource>();
		if (StringUtils.isBlank(path)) {
			return result;
		}
		final String absolutePath = currentExecutionContext.getAbsolutePath(path);
		Resource resource = resourceResolver.getResource(absolutePath);
		if (null == resource) {
			return result;
		}
		for (Iterator<Resource> iterator = resource.listChildren(); iterator.hasNext();) {
			result.add(iterator.next());
		}
		return result;
	}
}
