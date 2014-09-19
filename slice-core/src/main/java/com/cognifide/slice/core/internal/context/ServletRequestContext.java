/*-
 * #%L
 * Slice - Core
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2012 - 2014 Cognifide Limited
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

package com.cognifide.slice.core.internal.context;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import com.cognifide.slice.api.context.Context;
import com.google.inject.Key;

/**
 * @author Rafał ServletRequestContext
 * 
 * Context that stored all data in ServletRequest attributes.
 */
public class ServletRequestContext implements Context {

	private final Map<Key<?>, Object> contextMap;

	public ServletRequestContext(final String injectorName, final ServletRequest request) {
		final String attributeName = getInjectorAttributeName(injectorName);
		@SuppressWarnings("unchecked")
		Map<Key<?>, Object> contextMap = (Map<Key<?>, Object>) request.getAttribute(attributeName);
		if (contextMap == null) {
			request.setAttribute(attributeName, contextMap = new HashMap<Key<?>, Object>());
		}
		this.contextMap = contextMap;
	}

	@Override
	public <T> boolean contains(Key<T> key) {
		return contextMap.containsKey(key);
	}

	@Override
	public <T> void put(final Key<T> key, final T object) {
		contextMap.put(key, object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(final Key<T> key) {
		return (T) contextMap.get(key);
	}

	private static String getInjectorAttributeName(String injectorName) {
		return String.format("%s_%s", ServletRequestContext.class.getName(), injectorName);
	}
}
