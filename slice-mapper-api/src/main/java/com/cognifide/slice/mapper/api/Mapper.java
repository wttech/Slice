/*-
 * #%L
 * Slice - Mapper API
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

package com.cognifide.slice.mapper.api;

import org.apache.sling.api.resource.Resource;

/**
 * Mapper is an object that provides an abstract interface to content repository, providing some specific
 * operations without exposing details of the repository. It provides a mapping from application calls to the
 * persistence layer. This isolation separates the concerns of what data accesses the application needs, in
 * terms of domain-specific objects and data types (the public interface of the Mapper), and how these can be
 * satisfied with a repository (the implementation of the Mapper).
 */
public interface Mapper {

	/**
	 * Maps specified resource to specified object
	 * 
	 * @param resource resource representing repository node, cannot be null
	 * @param object an empty object to be mapped
	 * @return specified object with appropriate fields mapped from specified resource
	 */
	<T> T get(Resource resource, T object);

}
