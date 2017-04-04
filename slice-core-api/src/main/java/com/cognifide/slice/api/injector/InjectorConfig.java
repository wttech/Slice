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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Module;

/**
 * It contains all information necessary to create an injector or decide if given injector should be used to
 * handle given application. InjectorConfig is created for each instance of InjectorRunner. It also represents
 * an OSGi service (each InjectorConfig is registered as OSGi service by its InjectorRunner during bundle
 * startup).
 * 
 */
public class InjectorConfig {

	public static final String DEFAULT_INJECTOR_PATH = "/apps/";

	private final List<Module> modules;

	private final String name;

	private final List<String> applicationPaths;

	private final String parentName;

	private final String basePackage;

	private final String bundleFilter;

	private final InjectorCreationFailListener listener;

	InjectorConfig(final InjectorRunner runner) {
		// we don't allow to change the module list after creating the configuration
		modules = Collections.unmodifiableList(new ArrayList<Module>(runner.getModules()));
		name = runner.getInjectorName();
		applicationPaths = getApplicationPaths(runner);
		parentName = runner.getParentName();
		basePackage = runner.getBasePackage();
		bundleFilter = runner.getBundleNameFilter();
		listener = runner;
	}

	private List<String> getApplicationPaths(InjectorRunner runner) {
		if (runner.getApplicationPaths() == null){
			return Collections.singletonList(DEFAULT_INJECTOR_PATH + name);
		}
		return runner.getApplicationPaths();
	}

	public String getName() {
		return name;
	}

	public String getParentName() {
		return parentName;
	}

	public boolean hasParent() {
		return StringUtils.isNotBlank(parentName);
	}

	public List<? extends Module> getModules() {
		return modules;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public String getBundleNameFilter() {
		return bundleFilter;
	}

	public List<String> getApplicationPaths() {
		return applicationPaths;
	}

	public InjectorCreationFailListener getListener() {
		return listener;
	}
}
