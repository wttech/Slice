/*-
 * #%L
 * Slice - Core API
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

package com.cognifide.slice.util;


import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

/**
 * Simple util for returning SlingHttpServletRequest and SlingHttpServletResponse instances from
 * ServletRequest and ServletResponse.
 */
public final class ServletRequestResponseUtil {

	private ServletRequestResponseUtil() {
	}

	public static SlingHttpServletRequest getSlingHttpServletRequest(final ServletRequest request) {
		ServletRequest servletRequest = request;
		while (!(servletRequest instanceof SlingHttpServletRequest)) {
			if (servletRequest instanceof ServletRequestWrapper) {
				servletRequest = ((ServletRequestWrapper) servletRequest).getRequest();
			} else {
				throw new IllegalStateException("request has wrong class");
			}
		}
		return (SlingHttpServletRequest) servletRequest;
	}

	public static SlingHttpServletResponse getSlingHttpServletResponse(final ServletResponse response) {
		ServletResponse servletResponse = response;
		while (!(servletResponse instanceof SlingHttpServletResponse)) {
			if (servletResponse instanceof ServletResponseWrapper) {
				servletResponse = ((ServletResponseWrapper) servletResponse).getResponse();
			} else {
				throw new IllegalStateException("response has wrong class");
			}
		}
		return (SlingHttpServletResponse) servletResponse;
	}

}
