/*-
 * #%L
 * Slice - Core
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

package com.cognifide.slice.commons.module;

import com.cognifide.slice.api.context.ContextScope;
import com.cognifide.slice.core.internal.provider.DirectContextScopeProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Key;

/**
 * Module that allows binding classes to ContextScope with ease.
 */
public abstract class ContextScopeModule extends AbstractModule {

	private final ContextScope contextScope;

	/**
	 * Create new instance of ContextScopeModule.
	 * 
	 * All classes bound by bindToContextScope() will be bound to contextScope instance.
	 * @param contextScope context scope
	 */
	public ContextScopeModule(final ContextScope contextScope) {
		this.contextScope = contextScope;
	}

	protected ContextScope getContextScope() {
		return contextScope;
	}

	/**
	 * @param key key to bind to ContextScope
	 * @param <T> class of the object
	 * 
	 * Binds class to ContextScope. Instances of binded class must be manually set in Context instance to be
	 * available in Injector.
	 */
	protected <T> void bindToContextScope(final Key<T> key) {
		bind(key).toProvider(new DirectContextScopeProvider<T>(contextScope, key));
	}

}
