package com.cognifide.slice.validation.api;

/*
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


import java.util.List;

/**
 * Model object is the domain-specific representation of the data upon which the application operates. Domain
 * logic adds meaning to raw data (for example, calculating whether today is the user's birthday, or the
 * totals, taxes, and shipping charges for shopping cart items).
 * 
 * The object can be in three states:
 * <ul>
 * <li><b>valid</b> - model can be displayed in the view,</li>
 * <li><b>invalid</b> - populated incorrectly; error message should be displayed in the view,</li>
 * <li><b>empty</b> - not populated at all; specific error message should be displayed in the view - most
 * likely user didn't do anything wrong yet, just created empty component and has to pick it up.</li>
 * </ul>
 * 
 * @author Jan Kuźniak
 */
public interface ValidatableObject {
	/**
	 * Validates object.
	 * 
	 * @return true if model object is valid, false otherwise.
	 */
	boolean isValid();

	/**
	 * Checks if a model object is empty. Empty model objects are most often invalid, but view reaction is
	 * different in this case.
	 * 
	 * @return true if model object is empty, false otherwise.
	 */
	boolean isBlank();

	/**
	 * @return list of error messages. Can be empty or null.
	 */
	List<ErrorMessage> getErrorMessages();

}
