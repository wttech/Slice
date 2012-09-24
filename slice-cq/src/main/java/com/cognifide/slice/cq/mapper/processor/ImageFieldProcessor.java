package com.cognifide.slice.cq.mapper.processor;

/*
 * #%L
 * Slice - CQ Add-on
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


import java.lang.reflect.Field;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import com.cognifide.slice.mapper.annotation.ImagePath;
import com.cognifide.slice.mapper.api.processor.FieldProcessor;
import com.day.cq.wcm.foundation.Image;

public class ImageFieldProcessor implements FieldProcessor {

	@Override
	public boolean accepts(Resource resource, Field field) {
		return field.getType().isAssignableFrom(Image.class) || field.isAnnotationPresent(ImagePath.class);
	}

	@Override
	public Object mapResourceToField(Resource resource, ValueMap valueMap, Field field, String propertyName) {
		Class<?> targetClass = field.getType();
		Image image = new Image(resource, propertyName);
		if (targetClass.isAssignableFrom(Image.class)) {
			return image;
		} // else

		if (targetClass.isAssignableFrom(String.class) && (image != null) && image.hasContent()) {
			image.setSelector(".img");
			return image.getSrc();
		} // else

		return null;
	}

}
