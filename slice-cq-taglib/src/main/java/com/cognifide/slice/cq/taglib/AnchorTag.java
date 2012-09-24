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
import java.io.Writer;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang.StringUtils;

import com.cognifide.slice.api.link.LinkExternalizer;
import com.cognifide.slice.commons.link.LinkBuilderImpl;
import com.cognifide.slice.commons.link.LinkExternalizerImpl;

/**
 * Renders HTML anchor but does not render empty attributes. In other words, the tag can have attribute
 * defined, but if it's value is empty, it will not be rendered.
 * 
 * Additionally, this tag supports externalisation of handles passed in a href attribute.
 * 
 * @author Albert Cenkier
 * @author Jan Kuźniak
 */
public class AnchorTag extends AbstractBodyTag {

	private static final long serialVersionUID = 2811258272922488176L;

	/** id attribute of the <tt>&lt;a&gt;</tt> tag - specifies a unique id for an element */
	private String id;

	/** class attribute of the <tt>&lt;a&gt;</tt> tag - specifies a class name for an element */
	private String clazz;

	/** title attribute of the <tt>&lt;a&gt;</tt> tag - specifies extra information about an element */
	private String title;

	/** target attribute of the <tt>&lt;a&gt;</tt> tag - specifies where to open the linked document */
	private String target;

	/** name attribute of the <tt>&lt;a&gt;</tt> tag - specifies the name of an anchor */
	private String name;

	/**
	 * rel attribute of the <tt>&lt;a&gt;</tt> tag - specifies the relationship between the current document
	 * and the linked document
	 */
	private String rel;

	/** onclick attribute of the <tt>&lt;a&gt;</tt> tag - script to be run on a mouse click */
	private String onclick;

	/** href attribute of the <tt>&lt;a&gt;</tt> tag - specifies the destination of a link */
	private String href;

	/** specifies extension to be used when externalising if a handle was passed as href */
	private String extension;

	/** specifies selector to be used when externalising if a handle was passed as href */
	private String selector;

	/** if true - the href value will be externalised */
	private boolean externalise = true;

	/**
	 * Appends attribute to a writer if attribute's value is not blank. The attribute is appended in a format:
	 * <tt>'&nbsp;attributeName="attributeValue"'</tt> (with a leading space and double quotes).
	 * 
	 * @param out output stream to append the attribute to
	 * @param attributeName name of the attribute to be appended
	 * @param attributeValue value of the attribute
	 * @throws IOException if an I/O error occurs.
	 */
	protected void apppendAttributeIfNotBlank(Writer out, String attributeName, String attributeValue)
			throws IOException {
		if (StringUtils.isNotBlank(attributeValue)) {
			out.append(" ").append(attributeName).append("=\"").append(attributeValue).append("\"");
		}
	}

	/** {@inheritDoc} */
	@Override
	public int doStartTag() throws JspTagException {
		if (isRender()) {
			try {
				String link = this.href;

				if (externalise) {
					final LinkBuilderImpl linkBuilder = new LinkBuilderImpl();
					linkBuilder.setPath(link);
					linkBuilder.setSelectorString(selector);
					linkBuilder.setExtension(extension);

					final LinkExternalizer linkExternalizer = new LinkExternalizerImpl(null);
					link = linkExternalizer.externalizeLink(linkBuilder.toLink()).toString();
				}

				JspWriter out = getJspWriter();
				out.append("<a");
				apppendAttributeIfNotBlank(out, "href", link);
				apppendAttributeIfNotBlank(out, "id", id);
				apppendAttributeIfNotBlank(out, "class", clazz);
				apppendAttributeIfNotBlank(out, "title", title);
				apppendAttributeIfNotBlank(out, "target", target);
				apppendAttributeIfNotBlank(out, "name", name);
				apppendAttributeIfNotBlank(out, "rel", rel);
				apppendAttributeIfNotBlank(out, "onclick", onclick);
				out.append(">");
			} catch (IOException e) {
				// should never occur
				logger.error("unexpected exception occured", e);
			}
		}
		return EVAL_BODY_INCLUDE;
	}

	/** {@inheritDoc} */
	@Override
	public int doEndTag() throws JspTagException {
		if (isRender()) {
			try {
				getJspWriter().append("</a>");
			} catch (IOException e) {
				logger.error("unexpected exception occured", e);
			}
		}
		return EVAL_PAGE;
	}

	// /////////////////////////////////////////////////////////////////////////
	// getters and setters
	// ///////////////////////////////////////////////////////////////////////

	/** @return the id */
	@Override
	public String getId() {
		return id;
	}

	/** @param id the id to set */
	@Override
	public void setId(String id) {
		this.id = id;
	}

	/** @return the clazz */
	public String getClazz() {
		return clazz;
	}

	/** @param clazz the clazz to set */
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	/** @return the title */
	public String getTitle() {
		return title;
	}

	/** @param title the title to set */
	public void setTitle(String title) {
		this.title = title;
	}

	/** @return the target */
	public String getTarget() {
		return target;
	}

	/** @param target the target to set */
	public void setTarget(String target) {
		this.target = target;
	}

	/** @return the name */
	public String getName() {
		return name;
	}

	/** @param name the name to set */
	public void setName(String name) {
		this.name = name;
	}

	/** @return the rel */
	public String getRel() {
		return rel;
	}

	/** @param rel the rel to set */
	public void setRel(String rel) {
		this.rel = rel;
	}

	/** @return the onclick */
	public String getOnclick() {
		return onclick;
	}

	/** @param onclick the onclick to set */
	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	/** @return the href */
	public String getHref() {
		return href;
	}

	/** @param href the href to set */
	public void setHref(String href) {
		this.href = href;
	}

	/** @return the extension */
	public String getExtension() {
		return extension;
	}

	/** @param extension the extension to set */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/** @return the selector */
	public String getSelector() {
		return selector;
	}

	/** @param selector the selector to set */
	public void setSelector(String selector) {
		this.selector = selector;
	}

	/** @return the externalise */
	public boolean isExternalise() {
		return externalise;
	}

	/** @param externalise the externalise to set */
	public void setExternalise(boolean externalise) {
		this.externalise = externalise;
	}
}
