package com.cognifide.slice.cq;

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

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import com.cognifide.slice.api.scope.ContextScoped;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.inject.Inject;

@ContextScoped
public class TemplateManager {

	private final PageManager pageManager;

	@Inject
	public TemplateManager(final PageManager pageManager) {
		this.pageManager = pageManager;
	}

	public String getName(final String pagePath) {
		String templatePath = getPath(pagePath);
		return StringUtils.substringAfterLast(templatePath, "/");
	}

	public String getPath(final String pagePath) {
		String templateName = null;
		if (StringUtils.isNotBlank(pagePath)) {
			Page page = pageManager.getPage(pagePath);
			templateName = getPath(page);
		}
		return templateName;
	}

	public String getPath(final Page page) {
		String templateName = null;
		if (page != null) {
			Resource contentResource = page.getContentResource();
			if (contentResource != null) {
				ValueMap valueMap = contentResource.adaptTo(ValueMap.class);
				if (valueMap != null) {
					templateName = valueMap.get(NameConstants.PN_TEMPLATE, String.class);
				}
			}
		}
		return templateName;
	}

}
