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

package com.cognifide.slice.api.exceptions;


/**
 * Exception thrown when trying to get resource for Injector with invalid ExecutionContext. For example: using
 * relative paths when making first request to Injector.
 * 
 * @author Rafał Malinowski
 */
public class InvalidExecutionContextException extends RuntimeException {

	private static final long serialVersionUID = -5351461894519146986L;

	public InvalidExecutionContextException(final String string) {
		super(string);
	}

}
