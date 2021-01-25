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
package com.cognifide.slice.api.scope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cognifide.slice.mapper.annotation.SliceResource;
import com.google.inject.ScopeAnnotation;

/**
 * Indicates that an object is cached within context scope (see {@link ContextScoped}) for a current resource
 * path. This means that Slice will create only one instance of the class for a given path within single
 * request processing. Subsequent injections of the class will result with reusing of already created object.
 * This is especially useful for heavy {@link SliceResource} classes which map complex repository structures and which
 * don't change within single request invocation. <br>
 * <br>
 * It should be noted that if a resource which is mapped to a {@link SliceResource} class can change during request or
 * OSGi service invocation (ContextScope) then such a class cannot be annotated by the Cacheable annotation
 * as it may result in hard-to-track runtime errors.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ScopeAnnotation
public @interface Cacheable {
}
