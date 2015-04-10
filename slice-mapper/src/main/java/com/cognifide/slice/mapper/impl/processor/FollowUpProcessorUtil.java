/*-
 * #%L
 * Slice - Mapper
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

package com.cognifide.slice.mapper.impl.processor;

import java.lang.reflect.Field;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FollowUpProcessorUtil {

	private static final Logger LOG = LoggerFactory.getLogger(FollowUpProcessorUtil.class);

	/**
	 * Finds a resource referenced by path kept in specified resource under a property of specified name.
	 * 
	 * @param resource - a resource
	 * @param valueMap - map of all jcr properties values this resource
	 * @param field - the result of this method will be injected into this field, this parameter is not
	 * affecting the logic of this method but is needed for log information
	 * @param propertyName - name of jcr property
	 * @return the result resource if any found or null otherwise
	 */
	public static Resource getFollowUpResource(Resource resource, ValueMap valueMap, Field field,
			String propertyName) {
		final Class<?> fieldType = field.getType();
		Object value = getValue(valueMap, propertyName);

		if (value == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Property [{}/{}] is undefined, assigning null value for [{}#{}]", new Object[] {
						resource.getPath(), propertyName, fieldType.getCanonicalName(), field.getName() });
			}
			return null;
		}

		if (!(value instanceof String)) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("Property [{}/{}] is not of String type as required by "
						+ "@Follow in the model, assigning null " + "value for [{}#{}]", new Object[] {
						resource.getPath(), propertyName, fieldType.getCanonicalName(), field.getName() });
			}
			return null;
		}

		String referencedResourcePath = (String) value;
		Resource followUpResource = resource.getResourceResolver().getResource(referencedResourcePath);

		if (followUpResource == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug(
						"A resource [{}] referenced by [{}/{}] doesn't exist, "
								+ "assigning null value for [{}#{}]",
						new Object[] { referencedResourcePath, resource.getPath(), propertyName,
								fieldType.getCanonicalName(), field.getName() });
			}
			return null;
		}
		return followUpResource;
	}

	private static Object getValue(ValueMap valueMap, String propertyName) {
		Object value = null;
		if (valueMap != null) {
			value = valueMap.get(propertyName);
		}
		return value;
	}
}
