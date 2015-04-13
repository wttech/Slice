/*-
 * #%L
 * Slice - Core API
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

package com.cognifide.slice.api.context;

import aQute.bnd.annotation.ProviderType;

import com.cognifide.slice.api.scope.ContextScoped;
import com.google.inject.Scope;

/**
 * @author Rafał Malinowski
 * 
 * This Guice {@link Scope} stores all objects in Context object which is provided by specified
 * {@link ContextProvider}. ContextProvider object is stored on per-thread basis, so each thread can have its
 * own Context (it is also possible to create ContextProvider that shares data between threads).
 * 
 * ContextProvider object can be changed at any time and Context object returning by this provider can change
 * too. Thanks to that objects that are scoped in context ({@link ContextScoped} can be stored per-thread,
 * per-request or even per-second. It only requires an appropriate implementation of ContextProvider and
 * Context interfaces.
 */
@ProviderType
public interface ContextScope extends Scope {

	/**
	 * Set ContextProvider for this scope.
	 * 
	 * @param contextProvider ContextProvider for this scope
	 */
	void setContextProvider(ContextProvider contextProvider);

	/**
	 * Return ContextProvider for this scope.
	 * 
	 * @return ContextProvider for this scope
	 */
	ContextProvider getContextProvider();

}
