package com.cognifide.slice.api.injector;

/*
 * #%L
 * Slice - Core API
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

// @formatter:off
/**
 * @author Witold Szczerba
 * @author Rafał Malinowski
 * @class InjectorsRepository
 * 
 * Helper class to get Injector instances. Injector can be retreived in thtree different ways:
 * <ul>
 * <li>using getInjector(String appName) - get standard Injector for application with appName</li>
 * <li>using getInjectorWithContextProvider(String appName, ContextProvider contextProvider) - get Injector
 * for application with appName with custom ContextProvider</li>
 * <li>using getInjectorWithContext(String appName, Context context) - get Injector for application with
 * appName with ContextProvider that will always return provided context</li>
 * </ul>
 */
// @formatter:on
public interface InjectorsRepository {

	/**
	 * Get InjectorWithContext for application with name appName. If Sluice is not running then
	 * IllegalStateException will be thrown. If Injector for given application is not register in OSGI then
	 * null will be returned.
	 * 
	 * @param injectorName name of required injector
	 * @return InjectorWithContext for given application
	 */
	InjectorWithContext getInjector(final String injectorName);

}
