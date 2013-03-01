package com.cognifide.slice.cq.impl;

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


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cognifide.slice.api.execution.ExecutionContextStack;
import com.cognifide.slice.api.scope.ContextScoped;
import com.cognifide.slice.cq.PageChildrenProvider;
import com.cognifide.slice.cq.RecursiveMode;
import com.day.cq.commons.Filter;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.inject.Inject;

@ContextScoped
public class PageChildrenProviderImpl implements PageChildrenProvider {

	private final ExecutionContextStack currentExecutionContext;

	private final PageManager pageManager;

	@Inject
	public PageChildrenProviderImpl(final PageManager pageManager,
			final ExecutionContextStack currentExecutionContext) {
		this.pageManager = pageManager;
		this.currentExecutionContext = currentExecutionContext;
	}

	@Override
	public List<String> getChildPages(final String path, final Filter<Page> filter,
			final RecursiveMode recurseMode) {
		if (StringUtils.isBlank(path)) {
			return new ArrayList<String>();
		}

		final String absolutePagePath = currentExecutionContext.getAbsolutePath(path);
		final Page page = pageManager.getPage(absolutePagePath);

		return getChildren(page, filter, recurseMode);
	}

	@Override
	public List<String> getChildPages(final String path, final RecursiveMode recurseMode) {
		return getChildPages(path, new AllPageFilter(), recurseMode);
	}

	private List<String> getChildren(final Page page, final Filter<Page> filter,
			final RecursiveMode recurseMode) {
		ArrayList<String> result = new ArrayList<String>();
		if (null == page) {
			return result;
		}

		final Iterator<Page> children = page.listChildren(filter);
		while (children.hasNext()) {
			final Page child = children.next();
			result.add(child.getPath());

			if (recurseMode == RecursiveMode.RECURSIVE) {
				result.addAll(getChildren(child, filter, recurseMode));
			}
		}

		return result;
	}

	private static final class AllPageFilter implements Filter<Page> {
		@Override
		public boolean includes(Page page) {
			return true;
		}
	}

}
