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
 * Represents single error message model object. Error message consists of a mandatory message text, and
 * optional throwable - if the error message was caused by an exception.
 * 
 * @deprecated It will be removed (along with whole Validation API) in next major version - custom solution
 * required
 * 
 * @author Marcin Cenkier
 * @author Jan Kuźniak
 */
@Deprecated
public interface ErrorMessage {

	String getMessage();

	Throwable getThrowable();

	ErrorLevel getErrorLevel();

}
