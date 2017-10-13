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

import com.cognifide.slice.api.qualifier.Nullable;
import com.cognifide.slice.mapper.annotation.RequestParameter;
import com.cognifide.slice.mapper.api.processor.FieldProcessor;
import com.google.inject.Inject;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import javax.servlet.ServletRequest;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

public class RequestParameterProcessor implements FieldProcessor {

    @Inject
    @Nullable
    private ServletRequest servletRequest;

    @Override
    public boolean accepts(final Resource resource, final Field field) {
        Class<?> fieldType = field.getType();
        return (Collection.class.isAssignableFrom(fieldType) || fieldType.equals(String.class)) && field.isAnnotationPresent(RequestParameter.class);
    }

    @Override
    public Object mapResourceToField(Resource resource, ValueMap valueMap, Field field, String propertyName) {
        if (servletRequest == null) {
            return null;
        }
        String parameterName = getParameterName(field);
        Class<?> fieldType = field.getType();
        if (Collection.class.isAssignableFrom(fieldType)) {
            String[] parameters = servletRequest.getParameterValues(parameterName);
            if (parameters != null) {
                return Arrays.asList(parameters);
            }
        } else {
            return servletRequest.getParameter(parameterName);
        }
        return null;
    }

    private String getParameterName(Field field) {
        final RequestParameter annotation = field.getAnnotation(RequestParameter.class);
        if ((annotation != null) && StringUtils.isNotBlank(annotation.value())) {
            return annotation.value();
        }
        return field.getName();
    }
}
