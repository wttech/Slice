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

package com.cognifide.slice.api.model;

import org.apache.sling.api.resource.Resource;

import aQute.bnd.annotation.ProviderType;

/**
 * This service finds out a model class from a given Slice-aware CQ component.
 * 
 * @author Tomasz Rękawek
 *
 */
@ProviderType
public interface ModelClassResolver {

	/**
	 * Get model class from a given resource type.
	 *
	 * @param resourceType Sling resource type String
	 * @return model class defined in the slice:model property or null if there is no such property
	 * @throws ClassNotFoundException if the defined class can't be found
	 */
	@Deprecated
	Class<?> getModelClass(String resourceType) throws ClassNotFoundException;

	/**
	 * Get model class from a given resource.
	 *
	 * @param resource Sling resource
	 * @return model class defined in the slice:model property or null if there is no such property
	 * @throws ClassNotFoundException if the defined class can't be found
	 */
	Class<?> getModelClass(Resource resource) throws ClassNotFoundException;
}
