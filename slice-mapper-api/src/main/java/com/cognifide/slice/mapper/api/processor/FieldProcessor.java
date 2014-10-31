/*-
 * #%L
 * Slice - Mapper API
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

package com.cognifide.slice.mapper.api.processor;

import java.lang.reflect.Field;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

/**
 * Defines if a field can be mapped to a specified field and performs actual mapping.
 * 
 */
public interface FieldProcessor {

	/**
	 * Returns <code>true</code> if the specified field can be mapped by this processor, <code>false</code>
	 * otherwise.
	 * 
	 * @param resource
	 * @param field
	 * @return
	 */
	boolean accepts(Resource resource, Field field);

	/**
	 * Returns value which should be set in specified field. The original value should be read from specified
	 * valueMap using specified propertyName
	 * 
	 * @param resource
	 * @param valueMap
	 * @param field
	 * @param propertyName
	 * @return
	 */
	Object mapResourceToField(Resource resource, ValueMap valueMap, Field field, String propertyName);

}
