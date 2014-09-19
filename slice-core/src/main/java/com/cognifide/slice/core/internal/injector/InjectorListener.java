/*-
 * #%L
 * Slice - Core
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2012 - 2014 Cognifide Limited
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

package com.cognifide.slice.core.internal.injector;

import com.cognifide.slice.api.injector.InjectorConfig;
import com.google.inject.Injector;

/**
 * OSGi services implementing this interface will be informed about events related to the Slice injector
 * lifecycle.
 * 
 * @author Tomasz Rękawek
 * 
 */
public interface InjectorListener {
	/**
	 * Called when Slice injector is successfully created.
	 * 
	 * @param injector Created injector
	 * @param config Configuration of the created injector
	 */
	void injectorCreated(Injector injector, InjectorConfig config);

	/**
	 * Called when Slice injector is destroyed.
	 * 
	 * @param injector Destroyed injector
	 * @param config Configuration of the destroyed injector
	 */
	void injectorDestroyed(Injector injector, InjectorConfig config);
}
