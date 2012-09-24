package com.cognifide.slice.core.internal.context;

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


import javax.servlet.ServletRequest;

import com.cognifide.slice.api.context.Context;
import com.google.inject.Key;

/**
 * @author Rafał ServletRequestContext
 *
 * Context that stored all data in ServletRequest attributes.
 */
public class ServletRequestContext implements Context {

	private final Object nullObject = new Object();

	private final ServletRequest request;

	public ServletRequestContext(final ServletRequest request) {
		this.request = request;
	}

	@Override
	public <T> boolean contains(Key<T> key) {
		return null != request.getAttribute(key.toString());
	}

	@Override
	public <T> void put(final Key<T> key, final T object) {
		if (null != object) {
			request.setAttribute(key.toString(), object);
		} else {
			request.setAttribute(key.toString(), nullObject);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(final Key<T> key) {
		final Object value = request.getAttribute(key.toString());
		if (value == nullObject) {
			return null;
		} else {
			return (T) value;
		}
	}

}
