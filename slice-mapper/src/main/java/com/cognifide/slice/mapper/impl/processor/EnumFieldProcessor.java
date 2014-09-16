/*-
 * #%L
 * Slice - Mapper
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

package com.cognifide.slice.mapper.impl.processor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import com.cognifide.slice.mapper.api.processor.FieldProcessor;

public class EnumFieldProcessor implements FieldProcessor {

	@Override
	public boolean accepts(Resource resource, Field field) {
		Class<?> type = field.getType();
		return type.isEnum();
	}

	@Override
	public Object mapResourceToField(Resource resource, ValueMap valueMap, Field field, String propertyName) {
		String value = valueMap.get(propertyName, String.class);
		if (value == null) {
			return null;
		}

		List<?> enumFields = Arrays.asList(field.getType().getEnumConstants());
		for (Object o : enumFields) {
			Enum<?> enumField = (Enum<?>) o;
			if (value.equals(enumField.name())) {
				return enumField;
			}
		}
		return null;
	}

}
