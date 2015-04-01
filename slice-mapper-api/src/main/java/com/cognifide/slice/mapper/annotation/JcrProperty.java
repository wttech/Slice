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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows mapper a resource stored under given jcr property. The path to a resource is the one indicated by the name
 * of the field or {@link JcrProperty}.
 *
 * <pre>
 * {@literal @}SliceResource(MappingStrategy.ANNOTATED)
 * public class ExampleModel {
 *
 *   private String pageTitle;
 *
 *   {@literal @}JcrProperty
 *   private String pagePath;
 *
 *   {@literal @}JcrProperty("description")
 *   private String pageDescription;
 * }
 * </pre>
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JcrProperty {

	/**
	 * Custom property name. If empty, property name is read from field's name.
	 * 
	 * @return
	 */
	String value() default "";

}
