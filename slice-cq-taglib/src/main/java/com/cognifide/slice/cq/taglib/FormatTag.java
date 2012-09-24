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


import java.io.Writer;
import java.util.Formatter;
import java.util.Locale;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

/**
 * Formats value given a format and optional locale. Format is specified using printf-style format strings.
 * 
 * @author Marcin Cenkier
 * @author Jan Kuźniak
 */
public class FormatTag extends AbstractBodyTag {
	private static final long serialVersionUID = 6142420426315191239L;

	/** value to be formatted */
	protected Object value;

	/** printf-style format string */
	protected String format;

	/** used to get locale-specific formatting */
	protected Locale locale;

	public FormatTag() {
		init();
	}

	/**
	 * Resets tag's state.
	 */
	private void init() {
		value = null;
		format = null;
	}

	/** {@inheritDoc} */
	@Override
	public void release() {
		super.release();
		init();
	}

	/** {@inheritDoc} */
	@Override
	public int doStartTag() throws JspException {
		if (StringUtils.isNotBlank(format)) {
			Writer out = getJspWriter();

			Formatter formatter;
			if (locale == null) {
				formatter = new Formatter(out);
			} else {
				formatter = new Formatter(out, locale);
			}

			formatter.format(format, value);
			formatter.flush();
			formatter.close();
		}
		return SKIP_BODY;
	}

	/** {@inheritDoc} */
	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE; // nothing more to do
	}

	/** @return the locale */
	public Locale getLocale() {
		return locale;
	}

	/** @param locale the locale to set */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/** @param format the format to set */
	public void setFormat(String format) {
		this.format = format;
	}

	/** @return the value */
	public Object getValue() {
		return value;
	}

	/** @param value the value to set */
	public void setValue(Object value) {
		this.value = value;
	}
}
