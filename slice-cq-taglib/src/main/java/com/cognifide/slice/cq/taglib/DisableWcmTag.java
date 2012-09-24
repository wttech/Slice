package com.cognifide.slice.cq.taglib;

/*
 * #%L
 * Slice - CQ Taglib
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

import com.day.cq.wcm.api.WCMMode;

public class DisableWcmTag extends AbstractBodyTag {

	private static final long serialVersionUID = -8045328616424811060L;

	private WCMMode mode;

	@Override
	public int doStartTag() throws JspException {
		this.mode = WCMMode.DISABLED.toRequest(getRequest());
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		if (null != mode) {
			mode.toRequest(getRequest());
		}

		return EVAL_PAGE;
	}

}
