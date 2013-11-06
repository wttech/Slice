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


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Module;

public class InjectorConfig {
	private final List<Module> modules;

	private final String name;

	private final String parentName;

	InjectorConfig(InjectorRunner runner) {
		// we don't allow to change the module list after creating the configuration
		modules = Collections.unmodifiableList(new ArrayList<Module>(runner.getModules()));
		name = runner.getInjectorName();
		parentName = runner.getParentName();
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
}
