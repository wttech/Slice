package com.cognifide.slice.validation;

/*
 * #%L
 * Slice - Core
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

import com.cognifide.slice.mapper.annotation.IgnoreProperty;
import com.cognifide.slice.validation.api.ErrorLevel;
import com.cognifide.slice.validation.api.ErrorMessage;
import com.cognifide.slice.validation.api.ValidatableModel;

public abstract class SimpleValidatableObject implements ValidatableModel {

	private final List<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();

	@IgnoreProperty
	private boolean blank;

	public abstract void validate();

	@Override
	public boolean isValid() {
		this.errorMessages.clear();

		validate();

		return this.errorMessages.isEmpty();
	}

	@Override
	public boolean isBlank() {
		return blank;
	}

	public void setBlank(boolean blank) {
		this.blank = blank;
	}

	@Override
	public List<ErrorMessage> getErrorMessages() {
		return errorMessages;
	}

	public void addErrorMessage(String message) {
		addMessage(message, null, ErrorLevel.ERROR);
	}

	public void addErrorMessage(String message, Throwable t) {
		addMessage(message, null, ErrorLevel.ERROR);
	}

	public void addWarningMessage(String message) {
		addMessage(message, null, ErrorLevel.WARNING);
	}

	public void addInformationMessage(String message) {
		addMessage(message, null, ErrorLevel.INFORMATION);
	}

	public void addMessage(String message, Throwable t, ErrorLevel errorLevel) {
		errorMessages.add(new ErrorMessageImpl(message, t, errorLevel));
	}
}
