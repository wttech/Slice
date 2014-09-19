/*-
 * #%L
 * Slice - Mapper
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

package com.cognifide.slice.mapper.impl.processor;

import java.lang.reflect.Field;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import com.cognifide.slice.mapper.api.processor.FieldProcessor;
import com.cognifide.slice.mapper.helper.ReflectionHelper;

public class BooleanFieldProcessor implements FieldProcessor {

	@Override
	public boolean accepts(Resource resource, Field field) {
		Class<?> propertyType = ReflectionHelper.getFieldType(field);
		return propertyType.equals(Boolean.class);
	}

	@Override
	public Object mapResourceToField(Resource resource, ValueMap valueMap, Field field, String propertyName) {
		Boolean result = null;
		if (field.getType().isPrimitive()) {
			result = Boolean.FALSE;
		}
		if (valueMap != null) {
			Object value = valueMap.get(propertyName);
			if (value instanceof Boolean) {
				result = (Boolean) value;
			} else if (value instanceof String) {
				String stringValue = valueMap.get(propertyName, String.class);
				if (stringValue != null) {
					stringValue = stringValue.toLowerCase();
					if ("true".equals(stringValue)) {
						result = Boolean.TRUE;
					} else if ("false".equals(stringValue)) {
						result = Boolean.FALSE;
					}
				}
			}
		}

		return result;
	}

}
