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

import org.apache.sling.api.resource.Resource;

import aQute.bnd.annotation.ConsumerType;

/**
 * Defines if a value can be processed and performs actual processing. Class which implements this interface
 * should be added to list of post processors in
 * com.cognifide.slice.mapper.MapperBuilder#addFieldPostProcessor(FieldPostProcessor fieldPostProcessor)
 */
@ConsumerType
public interface FieldPostProcessor {

	/**
	 * @return <code>true</code> if specified value can be processed by this post-processor,
	 * <code>false</code> otherwise.
	 * 
	 * @param resource resource being mapped to the object
	 * @param field field being mapped
	 * @param value value read from resource and being set to a field
	 */
	boolean accepts(final Resource resource, final Field field, final Object value);

	/**
	 * @return a processed object basing on specified value
	 * 
	 * @param resource resource being mapped to the object
	 * @param field field being mapped
	 * @param value value read from resource and being set to a fielde
	 */
	Object processValue(final Resource resource, final Field field, final Object value);

}
