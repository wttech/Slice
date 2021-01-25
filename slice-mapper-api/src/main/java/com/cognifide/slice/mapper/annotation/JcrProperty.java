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

package com.cognifide.slice.mapper.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a given field should be mapped from a resource/property stored under specified path. The
 * path to a resource/property is indicated by the name of the field or value of {@link JcrProperty} (if
 * specified). A field with this annotation will be mapped by {@link com.cognifide.slice.mapper.api.Mapper}
 * using dedicated {@link MappingStrategy}. By default {@link MappingStrategy#ANNOTATED} strategy is used.
 * Example:
 *
 * <pre>
 * {@literal @}SliceResource
 * public class ExampleModel {
 * 
 *   {@literal @}JcrProperty
 *   private String simpleProperty;
 * 
 *   {@literal @}JcrProperty("jcr:title")
 *   private String pageTitle;
 * }
 * </pre>
 * 
 * In above example the field called {{simpleProperty}} will be mapped from a corresponding property called
 * {{simpleProperty}}, however {{pageTitle}} field will be mapped from {{jcr:title}} property of a resource.<br>
 * <br>
 * 
 * Please note that using of relative paths is allowed, e.g. "../parentNode/sibling2", "./child/grandChild"
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JcrProperty {

	/**
	 * Custom property name. If empty, property name is read from field's name. Relative paths are allowed
	 * 
	 * @return value
	 */
	String value() default "";

}
