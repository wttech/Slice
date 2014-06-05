package com.cognifide.slice.validation;

import java.util.ArrayList;
import java.util.List;

import com.cognifide.slice.validation.api.ErrorLevel;
import com.cognifide.slice.validation.api.ErrorMessage;
import com.cognifide.slice.validation.api.ValidationResult;
import com.cognifide.slice.validation.api.ValidationResultBuilder;
import com.cognifide.slice.validation.api.ValidationState;

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
 * Builds result of validation.
 * 
 * @deprecated It will be removed (along with whole Validation API) in next major version - custom solution
 * required
 * @author Rafał Malinowski
 */
public class ValidationResultBuilderImpl implements ValidationResultBuilder {

	private boolean blank = false;

	private final List<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();

	@Override
	public ValidationResultBuilder setBlank(boolean blank) {
		this.blank = blank;
		return this;
	}

	@Override
	public ValidationResultBuilder addErrorMessage(final String message) {
		addMessage(message, null, ErrorLevel.ERROR);
		return this;
	}

	@Override
	public ValidationResultBuilder addErrorMessage(final String message, final Throwable t) {
		addMessage(message, t, ErrorLevel.ERROR);
		return this;
	}

	@Override
	public ValidationResultBuilder addWarningMessage(final String message) {
		addMessage(message, null, ErrorLevel.WARNING);
		return this;
	}

	@Override
	public ValidationResultBuilder addInformationMessage(final String message) {
		addMessage(message, null, ErrorLevel.INFORMATION);
		return this;
	}

	@Override
	public ValidationResultBuilder addMessage(final String message, final Throwable t,
			final ErrorLevel errorLevel) {
		errorMessages.add(new ErrorMessageImpl(message, t, errorLevel));
		return this;
	}

	private ValidationState toValidationState() {
		if (blank) {
			return ValidationState.BLANK;
		}

		if (errorMessages.isEmpty()) {
			return ValidationState.VALID;
		}

		return ValidationState.INVALID;
	}

	@Override
	public ValidationResult toValidationResult() {
		return new ValidationResultImpl(toValidationState(), errorMessages);
	}

}
