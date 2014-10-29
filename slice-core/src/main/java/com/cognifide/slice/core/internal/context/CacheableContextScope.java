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

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

/**
 * This Guice Scope enables to cache slice models per request by using proper @Cacheable annotation on model
 * class
 * 
 * @author kamil.ciecierski
 */
public class CacheableContextScope implements Scope {

	private final Provider<CacheableContext> cacheableContextProvider;

	private final Provider<String> resourcePathProvider;

	public CacheableContextScope(Provider<CacheableContext> cacheableContextProvider,
			Provider<String> resourcePathProvider) {
		this.cacheableContextProvider = cacheableContextProvider;
		this.resourcePathProvider = resourcePathProvider;
	}

	@Override
	public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscopedProvider) {
		return new Provider<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T get() {
				CacheableContext cacheableContext = cacheableContextProvider.get();
				String resourcePath = resourcePathProvider.get();
				CacheableContextKey contextKey = new CacheableContextKey(resourcePath, key);
				T value = (T) cacheableContext.get(contextKey);
				if (value == null) {
					value = unscopedProvider.get();
					cacheableContext.put(contextKey, value);
				}
				return value;
			}
		};
	}

}
