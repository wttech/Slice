/*-
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

package com.cognifide.slice.api.context;

import com.google.inject.Key;

/**
 * @author Rafał Malinowski
 * 
 * Context for ContextScoped objects and Injectors.
 * 
 * This class is stores objects mapped by Key class. Null values can be stored. Use contains() to check if
 * given Key is available in Context. Calling get() and checking for null is not enough, because Context can
 * store null values as well.
 */
public interface Context {

	/**
	 * Check if value for given key is stored in this Context.
	 * 
	 * @param key key to check
	 * @return true if value for key is stored
	 */
	<T> boolean contains(Key<T> key);

	/**
	 * Put new object for given key. Object can be null.
	 * 
	 * @param key key to store new object under
	 * @param object new object to store or null
	 */
	<T> void put(Key<T> key, T object);

	/**
	 * Get value stored under given key. If no value or null value was stored this method will return null.
	 * Use contains() to differentiate between these two cases.
	 * 
	 * @param key key to get value for
	 * @return value for given key
	 */
	<T> T get(Key<T> key);

}
