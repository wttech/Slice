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
 * Builds result of validation. Errors messages can be added using addErrorMessage, addWarningMessage,
 * addInformationMessage and generic addMessage method.
 * 
 * If validated object is blank, use setBlank method.
 * 
 * @deprecated It will be removed (along with whole Validation API) in next major version - custom solution
 * required
 * @author Rafał Malinowski
 * 
 */
@Deprecated
public interface ValidationResultBuilder {

	ValidationResultBuilder setBlank(boolean blank);

	ValidationResultBuilder addErrorMessage(String message);

	ValidationResultBuilder addErrorMessage(String message, Throwable t);

	ValidationResultBuilder addWarningMessage(String message);

	ValidationResultBuilder addInformationMessage(String message);

	ValidationResultBuilder addMessage(String message, Throwable t, ErrorLevel errorLevel);

	ValidationResult toValidationResult();

}
