/*-
 * #%L
 * Slice - Core
 * %%
 * Copyright (C) 2012 Wunderman Thompson Technology
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

import com.cognifide.slice.api.context.ContextScope;
import com.google.inject.Key;
import com.google.inject.Provider;

/**
 * @author Rafał Malinowski
 * 
 * Generic provider for classes that are only stored manually in Context instances. This class requires
 * ContextScope to get Context from and get required instance from it.
 */
public class DirectContextScopeProvider<T> implements Provider<T> {

	private final ContextScope contextScope;

	private final Key<T> key;

	public DirectContextScopeProvider(final ContextScope contextScope, final Key<T> key) {
		this.contextScope = contextScope;
		this.key = key;
	}

	@Override
	public T get() {
		return (T) contextScope.getContextProvider().getContext().get(key);
	}

}
