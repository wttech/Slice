/*-
 * #%L
 * Slice - Core
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
package com.cognifide.slice.core.internal.module;

import java.util.List;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import com.cognifide.slice.api.injector.InjectorConfig;
import com.cognifide.slice.core.internal.injector.InjectorLifecycleListener;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;

/**
 * @author Krystian Panek
 * @class OsgiToGuiceAutoBindService
 * <p/>
 * This module class will ensure that all OSGi services can be injected.
 */
@Component(immediate = true)
@Service(value = InjectorLifecycleListener.class)
public class OsgiToGuiceAutoBindService implements InjectorLifecycleListener {

	private BundleContext bundleContext;

	protected String basePackage = "com.example.com"; // TODO to be configurable

	protected String bundleFilter = "example-app-.*"; // TODO to be configurable

	protected void activate(ComponentContext ctx) {
		this.bundleContext = ctx.getBundleContext();
	}

	@Override
	public void injectorCreating(List<Module> modules, InjectorConfig config) {
		final OsgiToGuiceAutoBindModule osgiModule = new OsgiToGuiceAutoBindModule(bundleContext,
				bundleFilter, basePackage);
		for (Module module : modules) {
			Modules.override(module, osgiModule);
		}
	}

	@Override
	public void injectorCreated(Injector injector, InjectorConfig config) {
		// do nothing
	}

	@Override
	public void injectorDestroyed(Injector injector, InjectorConfig config) {
		// do nothing
	}
}
