package com.cognifide.slice.api.tag;

/*
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


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;

public class SliceLookupTag extends SimpleTagSupport {

	private String var;

	private String appName; // auto-detected when null

	private Class<?> type;

	private void clean() {
		type = null;
		var = null;
		appName = null;
	}

	@Override
	public void doTag() throws JspException {
		try {
			if (StringUtils.isBlank(var) || (type == null)) {
				return;
			}

			final PageContext pageContext = (PageContext) getJspContext();
			final Object model = SliceTagUtils.getFromCurrentPath(pageContext, type, appName);
			pageContext.setAttribute(var, model, PageContext.PAGE_SCOPE);
		} finally {
			clean();
		}
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

}
