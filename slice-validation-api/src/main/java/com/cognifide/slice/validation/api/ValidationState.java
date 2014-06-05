package com.cognifide.slice.validation.api;

/*-
 * #%L
 * Slice - Core API
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

/**
 * State of validation. It can have one of three states:
 * 
 * <ul>
 * <li><b>valid</b> - validation passed,</li>
 * <li><b>invalid</b> - validation not passed, object is invalid,</li>
 * <li><b>blank</b> - not populated at all; specific error message should be displayed in the view - most
 * likely user didn't do anything wrong yet, just created empty component and has to pick it up.</li>
 * </ul>
 * 
 * @deprecated It will be removed (along with whole Validation API) in next major version - custom solution
 * required
 * 
 * @author Rafał Malinowski
 */
@Deprecated
public enum ValidationState {

	/**
	 * Object is not populated at all. This is different than invalid as most likely user didn't do anything
	 * wrong yet, just created empty object and has to pick it up.
	 */
	BLANK,

	/**
	 * Validation not passed, error messages are possible.
	 */
	INVALID,

	/**
	 * Validation passed.
	 */
	VALID;

	public boolean isBlank() {
		return BLANK == this;
	}

	public boolean isValid() {
		return VALID == this;
	}

}
