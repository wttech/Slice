/*-
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

package com.cognifide.slice.core.internal.context;


import java.util.HashMap;
import java.util.Map;

import com.cognifide.slice.api.context.Context;
import com.google.inject.Key;

/**
 * @author Rafał Malinowski
 *
 * Context that stored all data in itself.
 */
public class MapContext implements Context {

	private final Object nullObject = new Object();

	private final Map<String, Object> objects = new HashMap<String, Object>();

	@Override
	public <T> boolean contains(Key<T> key) {
		return objects.containsKey(key.toString());
	}

	@Override
	public <T> void put(final Key<T> key, final T object) {
		if (null != object) {
			objects.put(key.toString(), object);
		} else {
			objects.put(key.toString(), nullObject);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(final Key<T> key) {
		final Object value = objects.get(key.toString());
		if (value == nullObject) {
			return null;
		} else {
			return (T) value;
		}
	}

}
