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


import java.util.List;

import com.day.cq.commons.Filter;
import com.day.cq.wcm.api.Page;

/**
 * Provides methods for accessing child pages.
 * 
 */
public interface PageChildrenProvider {

	/**
	 * Returns child pages of a page under specified path. If a page under specified path doesn't exist, empty
	 * list is returned. The result list is filtered using specified filter.
	 * 
	 * @param path page path
	 * @param filter filter to limit the result list
	 * @param recurseMode defines if pages should be read recursively or only direct children should be
	 * returned
	 * @return filtered list of child pages
	 */
	List<String> getChildPages(final String path, Filter<Page> filter, RecursiveMode recurseMode);

	/**
	 * Returns child pages of a page under specified path. If a page under specified path doesn't exist, empty
	 * list is returned.
	 * 
	 * @param path page path
	 * @param recurseMode defines if pages should be read recursively or only direct children should be
	 * returned
	 * @return filtered list of child pages
	 */
	List<String> getChildPages(final String path, final RecursiveMode recurseMode);

}