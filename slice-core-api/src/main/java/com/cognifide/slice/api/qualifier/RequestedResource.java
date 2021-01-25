/*-
 * #%L
 * Slice - Core API
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

package com.cognifide.slice.api.qualifier;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

/**
 * Get resource requested by end user. This is a resource request by initial call to Sling instance. Please
 * note a difference between requested and current resource. Current resource can be chagned by calling
 * ModelProvider methods, requested resource is always constant within single request processing
 * 
 * <pre>
 * {@literal @}SliceResource
 * public class ExampleModel {
 * 
 *   private Resource resource;
 * 
 *   {@literal @}Inject
 *   public ExampleModel({@literal @}RequestedResource Resource resource) {
 *      this.resource = resource;
 *   }
 * }
 * </pre>
 * 
 * @author Rafał Malinowski
 */
@BindingAnnotation
@Target({ FIELD, PARAMETER, METHOD })
@Retention(RUNTIME)
public @interface RequestedResource {

}
