package com.cognifide.slice.cq.utils;

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

import com.day.cq.commons.jcr.JcrConstants;

public class PathUtils {

	public String getPagePathFromResourcePath(final String resourcePath) {
		if (StringUtils.isEmpty(resourcePath)) {
			return StringUtils.EMPTY;
		}

		int jcrContentPosition = resourcePath.lastIndexOf("/" + JcrConstants.JCR_CONTENT);
		if (0 > jcrContentPosition) {
			return resourcePath; // we are on page path
		}
		return resourcePath.substring(0, jcrContentPosition);
	}

}
