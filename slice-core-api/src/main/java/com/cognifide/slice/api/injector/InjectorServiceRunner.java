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


import java.util.List;

import org.osgi.framework.BundleContext;

import com.cognifide.slice.api.context.ContextScope;
import com.google.inject.Module;

// @formatter:off
/**
 * @author Rafał Malinowski
 * @class InjectorServiceRunner
 * 
 * Helper class to create injector and run it as a service under given application name. Before service is run
 * all modules must be installed.
 */
// @formatter:on
public interface InjectorServiceRunner {

	ContextScope getContextScope();

	/**
	 * Install new Guice module. This method can be called only before start() was called. In other case
	 * exception will be thrown.
	 * 
	 * @param module module to install.
	 */
	void installModule(final Module module);

	/**
	 * Install list of Guice modules. This method can be called only before start() was called. In other case
	 * exception will be thrown.
	 * 
	 * @param modules modules to install.
	 */
	void installModules(final List<Module> modules);

	/**
	 * Return BundleContext for this Runner.
	 */
	BundleContext getBundleContext();

	/**
	 * Return InjectorName of created Injector.
	 */
	String getInjectorName();

	/**
	 * Create Injector and run it as service with application name provided in constructor. Injector is
	 * created with all modules installed before this call.
	 */
	void start();

}
