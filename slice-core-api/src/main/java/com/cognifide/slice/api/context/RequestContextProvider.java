/*-
 * #%L
 * Slice - Core API
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

package com.cognifide.slice.api.context;

import aQute.bnd.annotation.ProviderType;

/**
 * It can create and return {@link ContextProvider} dedicated for request processing - there will only be one
 * context (provided by this ContextProvider) for a given request and injector
 * 
 */
@ProviderType
public interface RequestContextProvider {
	/**
	 * Return context provider dedicated for request processing. Bindings are stored separately for each
	 * injector.
	 * 
	 * @param injectorName Injector name
	 * @return Context provider which always returns the same context for given request and injector name.
	 */
	ContextProvider getContextProvider(String injectorName);
}
