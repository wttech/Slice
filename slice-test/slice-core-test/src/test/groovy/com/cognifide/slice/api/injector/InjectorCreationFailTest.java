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

package com.cognifide.slice.api.injector;

import com.cognifide.slice.api.context.ContextScope;
import com.cognifide.slice.core.internal.context.SliceContextScope;
import com.cognifide.slice.core.internal.injector.InjectorHierarchy;
import com.cognifide.slice.core.internal.injector.InjectorsRepositoryService;
import com.cognifide.slice.core.internal.module.JcrModule;
import com.cognifide.slice.core.internal.module.SliceModule;
import com.cognifide.slice.core.internal.module.SliceResourceModule;
import com.cognifide.slice.core.internal.module.SlingModule;
import com.cognifide.slice.mapper.module.MapperModule;

import com.cognifide.slice.test.module.TestService;
import com.cognifide.slice.test.module.TestServiceImpl1;
import com.cognifide.slice.test.module.TestServiceImpl2;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by Jaromir Celejewski on 2015-04-24.
 */
@RunWith(MockitoJUnitRunner.class)
public class InjectorCreationFailTest{

	protected InjectorHierarchy injectorHierarchy;

	protected InjectorsRepositoryService repositoryService;

	protected SliceModule sliceModule;

	protected InjectorConfig config;

	protected Method bindConfigMethod;

	@Mock
	protected BundleContext bundleContext;

	@Mock
	protected Bundle bundle;

	@Before
	public void setup() {
		ContextScope contextScope = new SliceContextScope();
		List<Module> modules = new ArrayList<Module>();
		modules.add(sliceModule = new SliceModule(contextScope, null));
		modules.add(new SlingModule(contextScope));
		modules.add(new JcrModule());
		modules.add(new MapperModule());
		modules.add(new SliceResourceModule());
		modules.add(new AbstractModule() {
			@Override
			protected void configure() {
				bind(TestService.class).to(TestServiceImpl1.class);
				bind(TestService.class).to(TestServiceImpl2.class);
			}
		});

		//Preparing configurations for injectors
		final InjectorRunner injectorRunner = new InjectorRunner(bundleContext,
				"slice-test",
				"slice-test-app.*",
				"com.cognifide.example");

		injectorRunner.installModules(modules);
		config = new InjectorConfig(injectorRunner);

		//creation of InjectorsRepositoryService with InjectorHierarchy
		repositoryService = new InjectorsRepositoryService();
		injectorHierarchy = new InjectorHierarchy();
		try {
			injectHierarchyIntoRepositoryService(repositoryService, injectorHierarchy);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		try {
			bindConfigMethod = injectorHierarchy.getClass().getDeclaredMethod("bindConfig", InjectorConfig.class);
		} catch (NoSuchMethodException e) {
			//
		}
		bindConfigMethod.setAccessible(true);

	}

	private void injectHierarchyIntoRepositoryService(InjectorsRepositoryService repositoryService, InjectorHierarchy injectorHierarchy)
			throws NoSuchFieldException {
		Field f = repositoryService.getClass().getDeclaredField("injectors");
		f.setAccessible(true);
		try {
			f.set(repositoryService, injectorHierarchy);
		} catch (IllegalAccessException e) {
			//
		}
	}

	@Test
	public void testInjectorCreationFail() throws InvocationTargetException, IllegalAccessException {

		//when
		when(bundleContext.getBundle()).thenReturn(bundle);

		//then invoke injectorHierarchy.bindConfig()
		bindConfigMethod.invoke(injectorHierarchy, config);

		//verify that injector hasn't been registered
		Assert.assertNull(repositoryService.getInjector("slice-test"));
	}
}

