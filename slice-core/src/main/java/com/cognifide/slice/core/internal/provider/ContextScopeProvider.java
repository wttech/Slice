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

package com.cognifide.slice.core.internal.provider;

import com.cognifide.slice.api.context.Context;
import com.cognifide.slice.api.context.ContextProvider;
import com.cognifide.slice.api.context.ContextScope;
import com.google.inject.Key;
import com.google.inject.Provider;

public final class ContextScopeProvider<T> implements Provider<T> {

	private final ContextScope contextScope;

	private final Provider<T> unscopedProvider;

	private final Key<T> key;

	public ContextScopeProvider(final ContextScope contextScope, final Provider<T> unscopedProvider, final Key<T> key) {
		this.contextScope = contextScope;
		this.unscopedProvider = unscopedProvider;
		this.key = key;
	}

	@Override
	public T get() {
		final ContextProvider contextProvider = this.contextScope.getContextProvider();
		if (null == contextProvider) {
			return null;
		}

		final Context context = contextProvider.getContext();
		synchronized (context) {
			if (context.contains(key)) {
				return context.get(key);
			}

			final T t = unscopedProvider.get();
			context.put(key, t);

			return t;
		}
	}

}
