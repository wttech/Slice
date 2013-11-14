package com.cognifide.slice.mapper.impl.processor;

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

import java.lang.reflect.Field;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.commons.provider.SliceResourceProvider;
import com.cognifide.slice.mapper.annotation.SliceResource;
import com.cognifide.slice.mapper.api.processor.FieldProcessor;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class SliceResourceFieldProcessor implements FieldProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(SliceResourceFieldProcessor.class);

	@Inject
	private Provider<SliceResourceProvider> sliceResourceProvider;

	@Override
	public boolean accepts(Resource resource, Field field) {
		Class<?> type = field.getType();
		// additional checks of type for performance sake
		return type != String.class && !type.isPrimitive() && type.isAnnotationPresent(SliceResource.class);
	}

	@Override
	public Object mapResourceToField(Resource resource, ValueMap valueMap, Field field, String propertyName) {
		Resource nestedResource = resource.getChild(propertyName);
		final Class<?> fieldType = field.getType();
		// create instance only if nested resource isn't null
		if (nestedResource == null) {
			// nested SliceResources are not instantiated as empty - not to erase information about them not
			// being present; when such functionality is required, a separate logic should be implemented for
			// that
			if (LOG.isDebugEnabled()) {
				LOG.debug(
						"the nested resource [{}/{}] doesn't exist, assigning null value for [{}#{}]",
						new Object[] { resource.getPath(), propertyName, fieldType.getCanonicalName(),
								field.getName() });
			}
			return null;
		} else {
			return sliceResourceProvider.get().get(fieldType, nestedResource);
		}
	}

}
