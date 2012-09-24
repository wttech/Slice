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

import com.cognifide.slice.api.link.LinkExternalizer;
import com.cognifide.slice.commons.link.LinkBuilderImpl;
import com.cognifide.slice.commons.link.LinkExternalizerImpl;

/**
 * Externalises given handle using {@link LinkUtils#externaliseContentHandle(String, String, String)} (see
 * detailed description there);
 * 
 * @author Albert Cenkier
 * @author Jan Kuźniak
 * @see LinkUtils#externaliseContentHandle(String, String, String)
 */
public class ExternaliseTag extends AbstractBodyTag {
	private static final long serialVersionUID = -3498559033987132975L;

	/** handle to be externalised */
	protected String handle;

	/** selector selector if required, can be null or empty, should not contain dots */
	protected String selector;

	/** extension extension if required, can be null or empty, should not contain dots */
	protected String extension;

	public ExternaliseTag() {
		init();
	}

	private void init() {
		handle = null;
		extension = null;
		selector = null;
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
		if (isRender()) {
			try {
				final LinkBuilderImpl linkBuilder = new LinkBuilderImpl();
				linkBuilder.setPath(handle);
				linkBuilder.setSelectorString(selector);
				linkBuilder.setExtension(extension);

				final LinkExternalizer linkExternalizer = new LinkExternalizerImpl(null);
				getJspWriter().write(linkExternalizer.externalizeLink(linkBuilder.toLink()).toString());
			} catch (Exception e) {
				// should never occur
				logger.error("unexpected exception occured", e);
			}
		}

		return SKIP_BODY;
	}

	/** {@inheritDoc} */
	@Override
	public int doEndTag() throws JspException {
		// nothing to do here, should not be invoked anyway
		return EVAL_PAGE;
	}

	/** @return the handle */
	public String getHandle() {
		return handle;
	}

	/** @param handle the handle to set */
	public void setHandle(String handle) {
		this.handle = handle;
	}

	/** @return the selector */
	public String getSelector() {
		return selector;
	}

	/** @param selector the selector to set */
	public void setSelector(String selector) {
		this.selector = selector;
	}

	/** @return the extension */
	public String getExtension() {
		return extension;
	}

	/** @param extension the extension to set */
	public void setExtension(String extension) {
		this.extension = extension;
	}
}
