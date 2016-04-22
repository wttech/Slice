/*-
 * #%L
 * Slice - Core Tests
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
package com.cognifide.slice.core.internal.injector

import com.citytechinc.aem.prosper.specs.ProsperSpec
import com.cognifide.slice.api.context.ContextScope
import com.cognifide.slice.api.injector.InjectorConfig
import com.cognifide.slice.api.injector.InjectorRunner
import com.cognifide.slice.core.internal.context.SliceContextScope
import com.cognifide.slice.core.internal.module.JcrModule
import com.cognifide.slice.core.internal.module.SliceModule
import com.cognifide.slice.core.internal.module.SliceResourceModule
import com.cognifide.slice.core.internal.module.SlingModule
import com.cognifide.slice.mapper.module.MapperModule
import com.cognifide.slice.test.module.TestModule
import com.google.inject.Module
import spock.lang.Shared

import java.lang.reflect.Field
import java.lang.reflect.Method

class InjectorsTestSetup extends ProsperSpec{

	@Shared
	InjectorHierarchy injectorHierarchy

	@Shared
	InjectorsRepositoryService repositoryService

	@Shared
	SliceModule sliceModule

	def setup() {
		ContextScope contextScope = new SliceContextScope()
		List<Module> modules = new ArrayList<Module>()
		modules.add(sliceModule = new SliceModule(contextScope, null))
		modules.add(new SlingModule(contextScope))
		modules.add(new JcrModule())
		modules.add(new MapperModule())
		modules.add(new SliceResourceModule())
		modules.add(new TestModule())

		//Preparing configurations for injectors
		InjectorConfig config1 = getConfig1(modules)
		InjectorConfig config2 = getConfig2(modules)
		InjectorConfig config3 = getConfig3(modules)

		//creation of InjectorsRepositoryService with InjectorHierarchy
		repositoryService = new InjectorsRepositoryService()
		injectorHierarchy = new InjectorHierarchy()
		injectHierarchyIntoRepositoryService(repositoryService, injectorHierarchy)

		//creation and registration of injectors in the hierarchy
		Method bindConfigMethod = injectorHierarchy.getClass().getDeclaredMethod("bindConfig", InjectorConfig.class)
		bindConfigMethod.setAccessible(true)
		bindConfigMethod.invoke(injectorHierarchy, config1)
		bindConfigMethod.invoke(injectorHierarchy, config2)
		bindConfigMethod.invoke(injectorHierarchy, config3)

	}
	
	private InjectorConfig getConfig1(List modules) {
		final InjectorRunner injectorRunner1 = new InjectorRunner(null,
				"slice-test",
				"slice-test-app.*",
				"com.cognifide.example")
		injectorRunner1.installModules(modules)
		InjectorConfig config1 = new InjectorConfig(injectorRunner1)
		return config1
	}
	
	private InjectorConfig getConfig2(List modules) {
		final InjectorRunner injectorRunner2 = new InjectorRunner(null,
				"slice-test2",
				"slice-test-app2.*",
				"com.cognifide.example2")
		injectorRunner2.installModules(modules)
		injectorRunner2.setParentInjectorName("slice-test")
		InjectorConfig config2 = new InjectorConfig(injectorRunner2)
		return config2
	}

	private InjectorConfig getConfig3(List modules) {
		final InjectorRunner injectorRunner3 = new InjectorRunner(null,
				"slice-test/subtest",
				"slice-test-app3.*",
				"com.cognifide.example3")
		injectorRunner3.installModules(modules)
		injectorRunner3.setParentInjectorName("slice-test")
		InjectorConfig config3 = new InjectorConfig(injectorRunner3)
		return config3
	}

	private void injectHierarchyIntoRepositoryService(InjectorsRepositoryService repositoryService, InjectorHierarchy injectorHierarchy) {
		Field f = repositoryService.getClass().getDeclaredField("injectors")
		f.setAccessible(true)
		f.set(repositoryService, injectorHierarchy)
	}
}
