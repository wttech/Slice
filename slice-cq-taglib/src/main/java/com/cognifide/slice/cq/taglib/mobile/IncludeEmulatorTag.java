package com.cognifide.slice.cq.taglib.mobile;

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


import javax.servlet.jsp.JspTagException;

import com.cognifide.slice.cq.taglib.AbstractBodyTag;
import com.day.cq.wcm.mobile.api.device.DeviceGroup;

/**
 * Inserts CQ5.4 mobile emulator includes - to be used in the head section.
 * 
 * @author Jan Kuźniak
 */
public class IncludeEmulatorTag extends AbstractBodyTag {

	private static final long serialVersionUID = 6727907904192317011L;

	@Override
	public int doStartTag() throws JspTagException {
		if (isRender()) {
			try {
				final DeviceGroup deviceGroup = getRequest().adaptTo(DeviceGroup.class);
				if (null != deviceGroup) {
					deviceGroup.drawHead(pageContext);
				}
			} catch (Exception e) {
				// fail safe
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
