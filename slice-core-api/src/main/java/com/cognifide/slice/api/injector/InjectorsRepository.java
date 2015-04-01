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

package com.cognifide.slice.api.injector;

import aQute.bnd.annotation.ProviderType;

import com.google.inject.Injector;

/**
 * @author Witold Szczerba
 * @author Rafał Malinowski
 * @class InjectorsRepository
 * <p/>
 * Helper class to get Injector instances. Slice supports multiple injectors installed on one instance, with
 * each injector being tagged with name of the application that created it. By default, the application name
 * is the name of its folder under /apps.
 * <p/>
 * Injector can be retrieved in two different ways:
 * <ul>
 * <li>by specifying injector / application name ({@link #getInjector(String injectorName)}),</li>
 * <li>by specifying resource path for which an injector has been registered (
 * {@link #getInjectorForResource(String resourcePath)}).</li>
 * </ul>
 */
@ProviderType
public interface InjectorsRepository {

	/**
	 * Returns injector (with context) for specific application, or null if no such injector was registered.
	 * 
	 * @param injectorName name of required injector (same as application name)
	 * @return InjectorWithContext for given application
	 * @throws IllegalStateException if Slice is not running
	 */
	InjectorWithContext getInjector(final String injectorName);

	/**
	 * Returns name of the given injector.
	 * 
	 * @param injector
	 * @return
	 */
	String getInjectorName(Injector injector);

	/**
	 * Returns injector name for specified resource path. It looks for an injector name using "best match"
	 * approach, i.e. if there are two injectors registered, one for path "/apps/appname" and the other for
	 * path "/apps/appname/x" then for resourcePath "appname/x/(...)" it will return injector registered for
	 * "/apps/appname/x"; for resourcePath "appname/y/(...)" it will return injector registered for
	 * "/apps/appname". If no injector can be found for a given resourcePath, <code>null</code> is returned.
	 * 
	 * @return injector name if found, <code>null</code> otherwise
	 */
	String getInjectorNameForResource(final String resourcePath);
}
