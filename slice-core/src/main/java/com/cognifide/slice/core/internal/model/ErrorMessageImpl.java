package com.cognifide.slice.core.internal.model;

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


import com.cognifide.slice.api.model.ErrorLevel;
import com.cognifide.slice.api.model.ErrorMessage;

/**
 * Represents single error message model object. Error message consists of a mandatory message text, and
 * optional throwable - if the error message was caused by an exception.
 * 
 * @author Marcin Cenkier
 * @author Jan Kuźniak
 */
public class ErrorMessageImpl implements ErrorMessage {
	private String message;

	private Throwable throwable;

	private ErrorLevel errorLevel;

	/**
	 * Creates error message model object instance
	 * 
	 * @param message mandatory message text.
	 * @param throwable optional throwable (use if the error message was caused by an exception).
	 * @param errorLevel mandatory error level {@link com.cognifide.slice.api.model.ErrorLevel}
	 */
	public ErrorMessageImpl(String message, Throwable throwable, ErrorLevel errorLevel) {
		this.message = message;
		this.throwable = throwable;
		this.errorLevel = errorLevel;
	}

	/** @return the message */
	@Override
	public String getMessage() {
		return message;
	}

	/** @return the throwable */
	@Override
	public Throwable getThrowable() {
		return throwable;
	}

	/** @return the error level */
	@Override
	public ErrorLevel getErrorLevel() {
		return errorLevel;
	}
}
