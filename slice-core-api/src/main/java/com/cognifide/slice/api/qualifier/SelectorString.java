/*-
 * #%L
 * Slice - Core API
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

package com.cognifide.slice.api.qualifier;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

/**
 * Get URL selectors as string or <code>null</code> if the request URL does not contain selectors.
 *
 * <pre>
 * {@literal @}SliceResource
 * public class ExampleModel {
 *
 * private String selectors;
 *
 * {@literal @}Inject
 * public ExampleModel({@literal @}Nullable {@literal @}SelectorString String selectors) {
 *      this.selectors = selectors;
 *      }
 * }
 * </pre>
 *
 * @see <a
 * href="https://sling.apache.org/apidocs/sling5/org/apache/sling/api/request/RequestPathInfo.html#getSelectorString%28%29">
 * org.apache.sling.api.request.RequestPathInfo#getSelectorString()</a>
 */
@BindingAnnotation
@Target({ FIELD, PARAMETER, METHOD })
@Retention(RUNTIME)
public @interface SelectorString {

}
