package com.cognifide.slice.api.context;

/*
 * #%L
 * Slice - Core API
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


import com.google.inject.Scope;

/**
 * @author Rafał Malinowski
 * 
 * This Guice Scope stored all data in Context objects that are provided by ContextProvider instances.
 * ContextProvider object are stored per-thread basis, so each thread can have its own Context (it is possible
 * to create ContextProvider that shared data between thread, so this is not a requirement).
 * 
 * ContextProvider objects can be changed at any time and also Context objects that there providers are
 * providing can change. Thanks to that objects that are ContextScoped can be stored per-thread, per-request
 * or even per-second. It only requires good implementation of ContextProvider and Context interfaces.
 */
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
