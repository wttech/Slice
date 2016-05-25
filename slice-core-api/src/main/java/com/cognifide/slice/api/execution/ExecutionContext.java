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

package com.cognifide.slice.api.execution;

import org.apache.sling.api.resource.Resource;

import com.cognifide.slice.api.provider.ModelProvider;

import aQute.bnd.annotation.ProviderType;

/**
 * This interface specifies the current resource (and/or path to this resource) that Injector is using.
 * Current context can be changed by calling ModelProvider with new path.
 * 
 * Path is always available. Resource can be null.
 * 
 * @deprecated This interface will be hidden from public use in next major version. Use {@link ModelProvider}
 * instead.
 */
@ProviderType
@Deprecated
public interface ExecutionContext {

	/**
	 * @return Resource of this ExecutionItem. This value can be null.
	 */
	Resource getResource();

	/**
	 * @return path of this ExecutionItem. This value can not be null.
	 */
	String getPath();

}
