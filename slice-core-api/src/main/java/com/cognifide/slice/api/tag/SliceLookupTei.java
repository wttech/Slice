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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class SliceLookupTei extends TagExtraInfo {

	private static final int VARIABLE_SCOPE = VariableInfo.AT_BEGIN;

	private static final boolean DECLARE_NEW_VARIABLE = true;

	private static final String ATTRIBUTE_TYPE = "type";

	private static final String ATTRIBUTE_VAR = "var";

	private static final String DEFAULT_TYPE = "java.lang.Object";

	private static final Pattern typePattern = Pattern.compile("<%=\\s*(.*).class\\s*%>");

	/**
	 * Get variable info for controller tag. This method adds additional info to object placed in the page
	 * context. In general, this additional info references to model class. This approach allows us to use
	 * Code Completion feature in our IDE's (works in Idea IntelliJ, not tested on Eclipse)
	 * 
	 * @param data Tag data
	 * @return Variable infos - info containing model class
	 */
	@Override
	public VariableInfo[] getVariableInfo(TagData data) {
		String type = String.valueOf(data.getAttribute(ATTRIBUTE_TYPE));
		Matcher matcher = typePattern.matcher(type);

		String className = DEFAULT_TYPE;
		if (matcher.matches()) {
			className = matcher.group(1);
		}

		String variableName = data.getAttributeString(ATTRIBUTE_VAR);

		return new VariableInfo[] {
				new VariableInfo(variableName, className, DECLARE_NEW_VARIABLE, VARIABLE_SCOPE) };
	}

}
