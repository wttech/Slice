package com.cognifide.slice.activator;

/*-
 * #%L
 * Slice - Activator
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

import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.cognifide.slice.api.injector.InjectorRunner;
import com.cognifide.slice.commons.SliceModulesFactory;
import com.google.inject.Module;

public class Activator implements BundleActivator {

	private static final String INJECTOR_NAME = "slice";

	private static final String BUNDLE_NAME_FILTER = "^slice-.+";

	private static final String BASE_PACKAGE = "com.cognifide.slice";

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		List<Module> sliceModules = SliceModulesFactory.createModules(bundleContext);

		InjectorRunner runner = new InjectorRunner(bundleContext, INJECTOR_NAME, BUNDLE_NAME_FILTER,
				BASE_PACKAGE);
		runner.installModules(sliceModules);
		runner.start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}
}
