/*-
 * #%L
 * Slice - Mapper API
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

package com.cognifide.slice.mapper.api.processor;

import java.lang.reflect.Field;

import org.apache.sling.api.resource.NonExistingResource;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.SyntheticResource;
import org.apache.sling.api.resource.ValueMap;

import aQute.bnd.annotation.ConsumerType;

/**
 * Defines if a field can be mapped to a specified field and performs actual mapping. Class which implements
 * this interface should be added to list of processors in
 * com.cognifide.slice.mapper.MapperBuilder#addFieldProcessor(FieldProcessor fieldProcessor)
 * 
 */
@ConsumerType
public interface FieldProcessor {

	/**
	 * @return <code>true</code> if the specified field can be mapped by this processor, <code>false</code>
	 * otherwise.
	 * 
	 * @param resource resource being mapped to the object
	 * @param field field being mapped
	 */
	boolean accepts(Resource resource, Field field);

	/**
	 * @return value which should be set in specified field. The original value should be read from specified
	 * valueMap using specified propertyName
	 * 
	 * @param resource resource being mapped to the object
	 * @param valueMap obtained from resource - may be null, if resource is a {@link SyntheticResource} or
	 * {@link NonExistingResource}
	 * @param field field being mapped
	 * @param propertyName name of the property to which a value should be assigned to.
	 */
	Object mapResourceToField(Resource resource, ValueMap valueMap, Field field, String propertyName);

}
