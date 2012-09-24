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


import java.io.PrintWriter;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TryCatchFinally;

import org.apache.commons.lang.StringUtils;

/**
 * Catches exceptions and renders their stack traces surrounded with <tt>&lt;pre&gt;</tt> tags, or as HTML
 * comments.
 * 
 * @author Albert Cenkier
 * @author Jan Kuźniak
 */
public class CatchTag extends AbstractBodyTag implements TryCatchFinally {
	private static final long serialVersionUID = 3338066321193727755L;

	/** specifies name under which throwable variable is stored */
	private String throwableVariableName;

	public CatchTag() {
		init();
	}

	/** {@inheritDoc} */
	@Override
	public void release() {
		super.release();
		init();
	}

	private void init() {
		throwableVariableName = null;
	}

	/** {@inheritDoc} */
	@Override
	public int doStartTag() {
		// in case previous instance set a throwable under the same name
		if (StringUtils.isNotBlank(throwableVariableName)) {
			pageContext.removeAttribute(throwableVariableName, PageContext.PAGE_SCOPE);
		}
		return EVAL_BODY_INCLUDE;
	}

	/** {@inheritDoc} */
	@Override
	public void doCatch(Throwable t) {
		if (StringUtils.isNotBlank(throwableVariableName)) {
			pageContext.setAttribute(throwableVariableName, t, PageContext.PAGE_SCOPE);
		}

		logger.error("Exception in JSP", t);
		if (isRender()) {
			PrintWriter out = new PrintWriter(pageContext.getOut());
			out.println("<pre class=\"stacktrace\">");
			t.printStackTrace(out);
			out.println("</pre>");
		}
	}

	/** {@inheritDoc} */
	@Override
	public void doFinally() {
		// nothing to do here
	}

	/** @return the throwableVariableName */
	public String getThrowableVariableName() {
		return throwableVariableName;
	}

	/** @param throwableVariableName the throwableVariableName to set */
	public void setThrowableVariableName(String throwableVariableName) {
		this.throwableVariableName = throwableVariableName;
	}
}
