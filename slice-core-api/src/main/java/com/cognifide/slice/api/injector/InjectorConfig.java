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

public class InjectorConfig {

	public static String DEFAULT_INJECTOR_PATH = "/apps/";

	private final List<Module> modules;

	private final String name;

	private final String path;

	private final String parentName;

	private final String basePackage;

	private final String bundleFilter;

	InjectorConfig(InjectorRunner runner) {
		// we don't allow to change the module list after creating the configuration
		modules = Collections.unmodifiableList(new ArrayList<Module>(runner.getModules()));
		name = runner.getInjectorName();
		path = StringUtils.isEmpty(runner.getInjectorPath()) ? DEFAULT_INJECTOR_PATH : runner.getInjectorPath();
		parentName = runner.getParentName();
		basePackage = runner.getBasePackage();
		bundleFilter = runner.getBundleNameFilter();
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

	public List<Module> getModules() {
		return modules;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public String getBundleNameFilter() {
		return bundleFilter;
	}

	public String getPath() {
		return path;
	}
}
