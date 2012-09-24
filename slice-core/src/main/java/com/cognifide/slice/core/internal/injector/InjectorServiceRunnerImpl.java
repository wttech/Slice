package com.cognifide.slice.core.internal.injector;

/*
 * #%L
 * Slice - Core
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


import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.osgi.framework.BundleContext;

import com.cognifide.slice.api.context.ContextScope;
import com.cognifide.slice.api.injector.InjectorServiceRunner;
import com.cognifide.slice.core.internal.context.SliceContextScope;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class InjectorServiceRunnerImpl implements InjectorServiceRunner {

	private final BundleContext bundleContext;

	private final String injectorName;

	private final ContextScope contextScope;

	private final List<Module> modules = new ArrayList<Module>();

	private boolean started = false;

	public InjectorServiceRunnerImpl(final BundleContext bundleContext, final String injectorName) {
		this.bundleContext = bundleContext;
		this.injectorName = injectorName;
		this.contextScope = new SliceContextScope();
	}

	@Override
	public BundleContext getBundleContext() {
		return bundleContext;
	}

	@Override
	public String getInjectorName() {
		return injectorName;
	}

	@Override
	public ContextScope getContextScope() {
		return contextScope;
	}

	@Override
	public void installModule(final Module newModule) {
		if (started) {
			throw new IllegalStateException("Installing new modules is not allowed after Injector was stared");
		}
		modules.add(newModule);
	}

	@Override
	public void installModules(final List<Module> newModules) {
		if (started) {
			throw new IllegalStateException("Installing new modules is not allowed after Injector was stared");
		}
		modules.addAll(newModules);
	}

	@Override
	public void start() {
		final Injector injector = Guice.createInjector(modules);

		final Dictionary<String, String> injectorAttr = new Hashtable<String, String>();
		injectorAttr.put("name", injectorName);

		bundleContext.registerService(Injector.class.getName(), injector, injectorAttr).getReference();

		started = true;
	}

}
