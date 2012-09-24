package com.cognifide.slice.mapper.annotation;

/*
 * #%L
 * Slice - Mapper API
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


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows for injection of models from a different location (path) than a current processing path. It is
 * useful to inject resources or models built on top of resources which lays in different location, not
 * necessarily child resources.<br>
 * <br>
 * 
 * It takes a path as a value. The value can be either an absolute path (like
 * <code>"/content/test/home"</code> or child path (like "childResource/grandChildResource") - then it will be
 * resolved relatively to current processing path - bear in mind that parent resources (like "../../test") are
 * not supported<br>
 * <br>
 * 
 * The value can contain placeholders in form of: <b>${placeholderName}</b>. The placeholders are resolved
 * using {@link SliceReferencePathResolver}, therefore a proper configuration of it is needed. A Guice module
 * must provide the SliceReferencePathResolver object which have all placeholders added using its
 * <code>addReference</code> methods. A simple snippet of code which provides the object can look like this:
 * 
 * <pre>
 * <code>@</code>Provides
 * <code>@</code>RequestScoped
 * public SliceReferencePathResolver getSliceReferencePathResolver(Injector injector) {
 * 	SliceReferencePathResolver pathResolver = new SliceReferencePathResolverImpl(injector);
 * 	pathResolver.addReference("home", "/content/test/home");
 * 	return pathResolver;
 * }
 * </code>
 * </pre>
 * 
 * @author maciej.majchrzak
 * 
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SliceReference {

	/**
	 * Path to a resource for which the model will be built. It may contain placeholders.
	 * 
	 * @return
	 */
	String value();
}