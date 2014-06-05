/*-
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
package com.cognifide.slice.core.internal.provider;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.commons.classloader.DynamicClassLoaderManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.Bundle;

import com.cognifide.slice.api.context.ContextFactory;
import com.cognifide.slice.api.context.ContextScope;
import com.cognifide.slice.api.provider.ModelProvider;
import com.cognifide.slice.core.internal.module.JcrModule;
import com.cognifide.slice.core.internal.module.LinkModule;
import com.cognifide.slice.core.internal.module.SliceModule;
import com.cognifide.slice.core.internal.module.SlingModule;
import com.cognifide.slice.mapper.module.MapperModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;

@RunWith(MockitoJUnitRunner.class)
public class SliceClassToKeyMapperTest {

	private Injector injector;

	@Mock
	private ContextScope contextScope;

	@Mock
	private Bundle bundle;

	@Mock
	private Provider<DynamicClassLoaderManager> classLoaderManager;

	@Mock
	private DynamicClassLoaderManager manager;

	@Mock
	private ClassLoader classLoader;

	private ClassToKeyMapper mapper;

	@Before
	public void setUp() throws ClassNotFoundException {
		when(classLoaderManager.get()).thenReturn(manager);
		when(manager.getDynamicClassLoader()).thenReturn(classLoader);

		List<Module> modules = new ArrayList<Module>();
		modules.add(new SliceModule(contextScope, bundle));
		modules.add(new SlingModule(contextScope));
		modules.add(new JcrModule());
		modules.add(new LinkModule());
		modules.add(new MapperModule());

		injector = Guice.createInjector(modules);
		mapper = new ClassToKeyMapper(injector, classLoaderManager);
	}

	@Test
	public void testGetKeyForSimpleType() throws ClassNotFoundException {
		doReturn(ClassWithAnObviousConstructor.class).when(classLoader).loadClass(
				ClassWithAnObviousConstructor.class.getName());
		// use type not bound in a module - but with an obvious constructor
		testForClassName(ClassWithAnObviousConstructor.class);
	}

	@Test
	public void testGetKeyForKnownBinding() {
		// use interface bound in a module - but with a simple constructor so it passes mocks
		testForClassName(ContextFactory.class);
	}

	@Test
	public void testInternalCaching() throws ClassNotFoundException {
		String className = ModelProvider.class.getName();

		// known class
		Key<?> key = mapper.getKey(className);
		Mockito.verify(classLoader, Mockito.times(0)).loadClass(className);

		// some unknown class
		className = SliceClassToKeyMapperTest.class.getName();
		doReturn(SliceClassToKeyMapperTest.class).when(classLoader).loadClass(className);
		key = mapper.getKey(className);
		Key<?> secondInvocationKey = mapper.getKey(className);
		Mockito.verify(classLoader, Mockito.times(1)).loadClass(className);
		assertEquals("Expecting the same key returned", key, secondInvocationKey);
	}

	private void testForClassName(Class<?> testedClass) {
		String className = testedClass.getName();
		Object instance1 = injector.getInstance(testedClass);
		assertNotNull("instance of " + className + " fetched by key cannot be null", instance1);

		Key<?> key = mapper.getKey(className);
		assertNotNull("key for " + className + " cannot be null", key);

		Object instance2 = injector.getInstance(key);
		assertNotNull("instance of " + className + " fetched by type cannot be null", instance2);

		assertEquals("Expected same class, never mind the method", instance1.getClass(), instance2.getClass());
	}

	private static class ClassWithAnObviousConstructor {
		@Inject
		public ClassWithAnObviousConstructor(ContextFactory contextFactory) {
		}
	}
}
