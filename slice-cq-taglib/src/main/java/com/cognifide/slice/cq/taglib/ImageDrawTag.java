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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.day.cq.wcm.api.components.DropTarget;
import com.day.cq.wcm.foundation.Image;

public class ImageDrawTag extends TagSupport {

	private static final long serialVersionUID = 8649634583655503247L;

	private transient Image image;

	@Override
	public int doEndTag() throws JspException {
		final JspWriter out = pageContext.getOut();

		try {
			if (image != null) {
				image.addCssClass(DropTarget.CSS_CLASS_PREFIX + "image");
				image.setSelector(".img");
				image.draw(out);
			} else {
				out.print("<img src=\"/libs/cq/ui/resources/0.gif\" class=\"cq-dd-image cq-image-placeholder\">");
			}
		} catch (IOException e) {
			throw new JspException(e);
		}

		return super.doEndTag();
	}

	public void setImage(final Image image) {
		this.image = image;
	}
}