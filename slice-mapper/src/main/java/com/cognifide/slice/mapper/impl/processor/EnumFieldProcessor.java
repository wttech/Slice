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
