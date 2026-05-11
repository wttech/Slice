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
package com.cognifide.slice.mapper.annotation;

import java.lang.annotation.*;

/**
 * Indicates that a given field should be mapped from a request attribute . The
 * name of the request attribute is indicated by the name of the field or value of {@link RequestAttribute} (if
 * specified).
 * Example:
 *
 * <pre>
 * {@literal @}SliceResource
 * public class ExampleModel {
 *
 *   {@literal @}RequestAttribute
 *   private String myAttribute;
 *
 *   {@literal @}RequestAttribute("second-attribute")
 *   private Boolean secondAttribute;
 * }
 * </pre>
 *
 * @author roy.teeuwen
 *
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestAttribute {

    /**
     * Custom request attribute name. If empty, property name is read from field's name.
     *
     * @return value
     */
    String value() default "";
}
