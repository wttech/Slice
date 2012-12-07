/**
 * 
 */
package com.cognifide.slice.cq.taglib.include;

/*-
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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;

import com.cognifide.slice.cq.taglib.include.IncludeTag.DecorationMode;
import com.day.cq.wcm.api.components.Component;

/**
 * Represents tag specific's configuration stored in a component node. Has both default values and read logic.
 */
class ComponentConfiguration {

	/** name of the component's property for disabling WCM.= */
	private static final String DISABLE_WCM_PROPERTY = "cog:disableWcm";

	/** name of the component's property for decoration modes */
	private static final String DECORATION_MODES_PROPERTY = "cog:enableDecorationInModes";

	private static final String ADDITIONAL_CSS_CLASS_NAMES = "cog:additionalCssClassNames";

	private boolean disableWcm;

	private DecorationMode[] decorationModes;

	private String[] additionalCssClassNames;

	private final Logger logger;

	// used for logging purposes
	private String path;

	public ComponentConfiguration(Logger logger) {
		disableWcm = IncludeTag.DEFAULT_DISABLE_WCM;
		decorationModes = new DecorationMode[] { IncludeTag.DEFAULT_DECORATION_MODE };
		additionalCssClassNames = new String[0];
		this.logger = logger;
	}

	public void readFromComponent(Component component) {
		ValueMap valueMap = component.getProperties();
		if (valueMap == null) {
			logger.warn("Resource '{}' adapts to null value map, skipping", component.getPath());
		} else {
			readDisableWcm(valueMap.get(DISABLE_WCM_PROPERTY));
			readDecorationModes(valueMap.get(DECORATION_MODES_PROPERTY));
			readAdditionalCssClassNames(valueMap.get(ADDITIONAL_CSS_CLASS_NAMES));
		}
	}

	private void readDisableWcm(Object disableWcmProperty) {
		if (disableWcmProperty == null) {
			disableWcm = IncludeTag.DEFAULT_DISABLE_WCM;
		} else {
			// reading as String - while not the best performance-wise, it is the safest option
			disableWcm = "true".equalsIgnoreCase(disableWcmProperty.toString());
		}
	}

	private void readDecorationModes(Object decorationModeProperty) {
		if (decorationModeProperty != null) {
			if (decorationModeProperty instanceof Object[]) {
				readMultiValueDecorationModes(decorationModeProperty);
			} else {
				readSingleDecorationMode(decorationModeProperty);
			}
		}
	}

	private void readSingleDecorationMode(Object decorationModeProperty) {
		String decorationModeString = decorationModeProperty.toString();
		DecorationMode decorationMode = DecorationMode.fromString(decorationModeString);
		if (decorationMode == null) {
			String msg = "Unknown value of simple property: {}@{}='{}', default used";
			logger.warn(msg, new Object[] { path, DECORATION_MODES_PROPERTY, decorationModeProperty });
		} else {
			decorationModes = new DecorationMode[] { decorationMode };
		}
	}

	private void readMultiValueDecorationModes(Object decorationModeProperty) {
		Object[] decorationModeProperties = (Object[]) decorationModeProperty;
		List<DecorationMode> decorationModesList = new ArrayList<DecorationMode>();
		for (int i = 0; i < decorationModeProperties.length; i++) {
			Object property = decorationModeProperties[i];
			DecorationMode decorationMode = DecorationMode.fromObject(property);
			if (decorationMode == null) {
				String msg = "Unknown value #{} of multi-valued property, skipping: {}@{}[{}]='{}'";
				logger.warn(msg, new Object[] { i, path, DECORATION_MODES_PROPERTY, i, property });
			} else {
				decorationModesList.add(decorationMode);
			}
		}
		if (decorationModesList.isEmpty()) {
			String msg = "Empty multi-valued property, default used: {}@{}='{}'";
			logger.warn(msg, new Object[] { path, DECORATION_MODES_PROPERTY, decorationModeProperty });
		} else {
			decorationModes = decorationModesList.toArray(decorationModes);
		}
	}

	private void readAdditionalCssClassNames(Object additionalCssClassNamesProperty) {
		if (additionalCssClassNamesProperty != null) {
			if (additionalCssClassNamesProperty instanceof Object[]) {
				readMultiValueAdditionalCssClassNames(additionalCssClassNamesProperty);
			} else {
				readSingleAdditionalCssClassName(additionalCssClassNamesProperty);
			}
		}
	}

	private void readSingleAdditionalCssClassName(Object additionalCssClassNamesProperty) {
		String additionalCssClassNameString = additionalCssClassNamesProperty.toString();
		if (StringUtils.isBlank(additionalCssClassNameString)) {
			String msg = "Null value of simple property: {}@{}='{}', default used";
			logger.warn(msg,
					new Object[] { path, ADDITIONAL_CSS_CLASS_NAMES, additionalCssClassNamesProperty });
		} else {
			additionalCssClassNames = new String[] { additionalCssClassNameString };
		}
	}

	private void readMultiValueAdditionalCssClassNames(Object additionalCssClassNamesProperty) {
		Object[] additionalCssClassNameProperties = (Object[]) additionalCssClassNamesProperty;
		List<String> additionalCssClassList = new ArrayList<String>();
		for (int i = 0; i < additionalCssClassNameProperties.length; i++) {
			Object property = additionalCssClassNameProperties[i];
			String additionalCssClassNameString = property.toString();
			if (StringUtils.isBlank(additionalCssClassNameString)) {
				String msg = "Unknown value #{} of multi-valued property, skipping: {}@{}[{}]='{}'";
				logger.warn(msg, new Object[] { i, path, ADDITIONAL_CSS_CLASS_NAMES, i, property });
			} else {
				additionalCssClassList.add(additionalCssClassNameString);
			}
		}
		if (additionalCssClassList.isEmpty()) {
			String msg = "Empty multi-valued property, default used: {}@{}='{}'";
			logger.warn(msg,
					new Object[] { path, ADDITIONAL_CSS_CLASS_NAMES, additionalCssClassNamesProperty });
		} else {
			additionalCssClassNames = additionalCssClassList.toArray(additionalCssClassNames);
		}
	}

	/** @return the disableWcm */
	public boolean isDisableWcm() {
		return disableWcm;
	}

	/** @return the decorationModes */
	public DecorationMode[] getDecorationModes() {
		return decorationModes;
	}

	public String[] getAdditionalCssClassNames() {
		return additionalCssClassNames;
	}

}