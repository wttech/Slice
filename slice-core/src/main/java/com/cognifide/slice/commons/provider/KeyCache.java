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

package com.cognifide.slice.commons.provider;

import java.util.HashMap;
import java.util.Map;

import com.cognifide.slice.api.qualifier.EmptyObject;
import com.google.inject.Key;
import com.google.inject.Singleton;

@Singleton
public class KeyCache {
	private final Map<Class<?>, Key<?>> cache;

	public KeyCache() {
		cache = new HashMap<Class<?>, Key<?>>();
	}

	/**
	 * Create Guice Key object for given class and {@link EmptyObject} annotation. If such key has been
	 * already created, return cached value.
	 * 
	 * @param clazz Class to be wrapped by Key object
	 * @return Key object created using {@code clazz} parameter
	 */
	@SuppressWarnings("unchecked")
	public <T> Key<T> getKey(final Class<T> clazz) {
		Key<T> key = (Key<T>) cache.get(clazz);
		if (key == null) {
			key = Key.get(clazz, EmptyObject.class);
			cache.put(clazz, key);
		}
		return key;
	}
}
