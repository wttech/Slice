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
package com.cognifide.slice.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;
import com.google.inject.Key;

/**
 * @author Jaromir Celejewski
 * 
 * This annotation when used on fields or in constructors, informs Slice about OSGi services which should be
 * bound automatically to a Peaberry provider and therefore make them injectable. Please note that this is NOT
 * a {@link BindingAnnotation} there must not be used as a part of Guice's {@link Key}. It is supposed to be
 * used in conjunction with @Inject annotation, e.g:
 * 
 * <pre>
 * class SomeClass {
 *   &#064;Inject
 *   &#064;OsgiService
 *   private AnyOsgiService anyOsgiService;
 *   ...
 * }
 * </pre>
 * 
 * In above example, Slice automatically creates and binds (while creating application injector) a Peaberry
 * provider for the AnyOsgiService class. Therefore the AnyOsgiService can be injected in any class.
 * 
 * This functionality requires com.cognifide.slice.core.internal.module.OsgiToGuiceAutoBindModule to
 * be installed in injector.
 */

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface OsgiService {
}
