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

package com.cognifide.slice.api.scope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cognifide.slice.api.qualifier.Nullable;
import com.cognifide.slice.util.InjectorUtil;
import com.google.inject.ScopeAnnotation;

/**
 * This annotation should be used if you want your object to be scoped with use of Slice Context. The context
 * scope can be understood in two ways, depends on a way injector has been created:<br>
 * <ul>
 * <li>Request scope - object is scoped within request, meaning that an object is created once and shared
 * during single request processing. Effectively, it ensures one object instance of given class per request.
 * See {@link InjectorUtil#getInjector(String, org.apache.sling.api.SlingHttpServletRequest)}</li>
 * <li>
 * Thread scope - object is scoped within thread, usually background processing. When request is not available
 * (background processing, scheduled jobs, etc), you can still scope your objects for given processing. While
 * creating injector, you can apply context containing resource resolver, so all classes annotated by this
 * annotation will be created once per given processing. See
 * {@link InjectorUtil#getInjector(String, org.apache.sling.api.resource.ResourceResolver)}</li>
 * </ul>
 * By using context scope you can share your code between request processing and background processing - your
 * objects will be created only once and stored either within request attributes (if request is available) or
 * in thread local for background processing along with related resource resolver.<br>
 * <br>
 * 
 * Please note that your objects cannot rely on request-related objects (like request parameters, session,
 * attributes, etc) if you're code is going to be shared between request and background processing - such
 * request-related objects are just not available during background processing which will result in failing
 * code (unless you're injecting them with {@link Nullable} specifier).
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ScopeAnnotation
public @interface ContextScoped {
}
