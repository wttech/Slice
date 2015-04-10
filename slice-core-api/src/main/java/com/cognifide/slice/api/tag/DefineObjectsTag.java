/*-
 * #%L
 * Slice - Core API
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
package com.cognifide.slice.api.tag;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.sling.api.scripting.SlingBindings;

public class DefineObjectsTag extends SimpleTagSupport {

	public static final String MODEL_BINDING = "model";

	private static final String MODEL_NAME = "model";

	private String modelName = MODEL_NAME;

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	@Override
	public void doTag() throws JspException {
		try {
			final PageContext pageContext = (PageContext) getJspContext();
			final ServletRequest request = pageContext.getRequest();
			final SlingBindings bindings = (SlingBindings) request
					.getAttribute(SlingBindings.class.getName());
			pageContext.setAttribute(modelName, bindings.get(MODEL_BINDING));
		} finally {
			clean();
		}
	}

	private void clean() {
		modelName = MODEL_NAME;
	}

}
