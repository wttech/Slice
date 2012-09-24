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


import java.io.IOException;

import javax.servlet.jsp.JspTagException;

/**
 * Inserts <code>&lt;div style="clear:both"&gt;&#38;nbsp;&lt;/div&gt;</code> if the render attribute is set to
 * true.
 * 
 * @author Albert Cenkier
 * @author Jan Kuźniak
 */
public class ClearFloatTag extends AbstractBodyTag {
	private static final long serialVersionUID = 64260309540567509L;

	public static final String CLEAR_BOTH_DIV = "<div style=\"clear:both;font-size:1px\">&nbsp;</div>";

	@Override
	public int doStartTag() throws JspTagException {
		if (isRender()) {
			try {
				getJspWriter().append(CLEAR_BOTH_DIV);
			} catch (IOException e) {
				logger.error("Unexpected exception occured", e);
			}
		}

		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspTagException {
		return EVAL_PAGE;
	}
}
