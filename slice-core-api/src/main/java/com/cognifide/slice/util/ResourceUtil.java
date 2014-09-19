/*-
 * #%L
 * Slice - Core API
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

package com.cognifide.slice.util;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

/**
 * Contains methods for accessing resources - pieces of content on which Sling acts.
 * 
 * @author Jan Kuźniak
 * 
 */
public final class ResourceUtil {

	private ResourceUtil() {
		// hidden constructor
	}

	/**
	 * Validates resource - if valid, it's returned back. Null value is returned otherwise. Resource is valid
	 * if it can be adapted to a non null value map. This method is used to change empty resources to
	 * <code>null</code>
	 * 
	 * @param resource resource to be validated
	 * @return resource if valid, <code>null</code> otherwise
	 */
	public static Resource getValidResourceOrNull(Resource resource) {
		if ((resource == null) || (resource.adaptTo(ValueMap.class) == null)) {
			// simple validation - empty resources will return null value map -
			// so are null themselves
			return null;
		}
		return resource;
	}
}
