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
 * Created 19.09.14
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

	@SuppressWarnings("unchecked")
	@Override
	public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
		return new Provider<T>() {
			@Override
			public T get() {
				CacheableContext cacheableContext = cacheableContextProvider.get();
				String resourcePath = resourcePathProvider.get();
				T scoped = (T) cacheableContext.get(resourcePath);
				if (scoped == null) {
					scoped = unscoped.get();
					cacheableContext.put(resourcePath, scoped);
				}
				return scoped;
			}
		};
	}
}
