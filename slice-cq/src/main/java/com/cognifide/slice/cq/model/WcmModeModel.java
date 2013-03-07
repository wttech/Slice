package com.cognifide.slice.cq.model;

/*-
 * #%L
 * Slice - CQ Add-on
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

import com.day.cq.wcm.api.WCMMode;
import com.google.inject.Inject;

/**
 * Stores WCM mode properties. WCM mode is a state of the page - can be either one of:
 * <ul>
 * <li>DISABLED - (a.k.a publish mode), default mode on publish instance, no controls visible</li>
 * <li>EDIT - default mode on author instance, blue boxes visible</li>
 * <li>PREVIEW - default mode for author's preview, should render exactly what DISABLED does</li>
 * <li>READ_ONLY - default mode for author when a permission to write is not granted</li>
 * <li>DESIGN - allows setting e.g. paragraph system design properties on author instance</li>
 * <li>ANALYTICS - WCM is in analytics mode, added in AEM5.6</li>
 * </ul>
 * 
 * The class stores the raw WCM mode and a set of boolean variables to be set for ease of access in the view.
 * 
 * @see com.day.cq.wcm.api.WCMMode
 * @author Jan Kuźniak
 */
public class WcmModeModel {

	/**
	 * TODO what is this?
	 */
	public static final String REAL_AUTHOR = "REALAUTHOR";

	private final WCMMode wcmMode;

	/**
	 * Creates model instance.
	 * 
	 * @param wcmMode the WCM mode.
	 */
	@Inject
	public WcmModeModel(final WCMMode wcmMode) {
		this.wcmMode = wcmMode;
	}

	/** @return the native WCM mode */
	public WCMMode getWcmMode() {
		return wcmMode;
	}

	/** @return true if WCM is disabled, false otherwise */
	public boolean isDisabled() {
		return wcmMode == WCMMode.DISABLED;
	}

	/** @return true if WCM is in edit mode, false otherwise */
	public boolean isEdit() {
		return wcmMode == WCMMode.EDIT;
	}

	/** @return true if WCM is in preview mode, false otherwise */
	public boolean isPreview() {
		return wcmMode == WCMMode.PREVIEW;
	}

	/** @return true if WCM is in read only mode, false otherwise */
	public boolean isReadOnly() {
		return wcmMode == WCMMode.READ_ONLY;
	}

	/** @return true if WCM is in design, false otherwise */
	public boolean isDesign() {
		return wcmMode == WCMMode.DESIGN;
	}

	/** @return true if WCM is in analytics, false otherwise */
	public boolean isAnalytics() {
		return wcmMode == WCMMode.ANALYTICS;
	}

	/** @return true if WCM is in edit mode or design mode, false otherwise */
	public boolean isAuthor() {
		return (wcmMode == WCMMode.EDIT) || (wcmMode == WCMMode.DESIGN);
	}

	/** @return true if WCM is neither in edit mode nor design mode, false otherwise */
	public boolean isPublish() {
		return (wcmMode != WCMMode.EDIT) && (wcmMode != WCMMode.DESIGN);
	}

}
