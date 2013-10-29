package com.cognifide.slice.core.internal.provider;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.api.context.ContextScope;
import com.cognifide.slice.api.provider.ClassToKeyMapper;
import com.cognifide.slice.core.internal.module.ContextModule;
import com.cognifide.slice.core.internal.module.JcrModule;
import com.cognifide.slice.core.internal.module.LinkModule;
import com.cognifide.slice.core.internal.module.SliceModule;
import com.cognifide.slice.core.internal.module.SliceResourceModule;
import com.cognifide.slice.core.internal.module.SlingModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;

@RunWith(MockitoJUnitRunner.class)
public class SliceClassToKeyMapperTest {
	private static final String APPLICATION_NAME = "test";

	private static final String BUNDLE_NAME_FILTER = "com\\.cognifide\\.test\\.webapp\\..*";

	private static final String BASE_PACKAGE = "com.cognifide.test";

	private Injector injector;

	private static final Logger LOG = LoggerFactory.getLogger(SliceClassToKeyMapperTest.class);

	@Mock
	private BundleContext bundleContext;

	@Mock
	private Bundle bundle;

	@Mock
	private ContextScope contextScope;

	@Before
	public void setUp() throws ClassNotFoundException {
		when(bundleContext.getBundles()).thenReturn(new Bundle[0]);
		when(bundle.loadClass(ClassToKeyMapper.class.getCanonicalName())).thenReturn(SliceClassToKeyMapper.class);
		
		List<Module> modules = new ArrayList<Module>();
		modules.add(new SliceModule(APPLICATION_NAME, contextScope, bundle));
		modules.add(new SlingModule(contextScope));
		modules.add(new JcrModule());
		modules.add(new LinkModule());
		modules.add(new ContextModule());
		modules.add(new SliceResourceModule(bundleContext, BUNDLE_NAME_FILTER, BASE_PACKAGE));

		injector = Guice.createInjector(modules);
	}

	@Test
	public void testGetKey() {
		ClassToKeyMapper mapper = injector.getInstance(ClassToKeyMapper.class);
		System.out.println(getClass().getCanonicalName());
		testForClassName(mapper, ClassToKeyMapper.class, ClassToKeyMapper.class.getName());
	}

	private void testForClassName(ClassToKeyMapper mapper, Class<?> clazz, String className) {
		LOG.error("test");
		Object instance1 = injector.getInstance(clazz);
		assertNotNull("instance of " + className + " fetched by key cannot be null", instance1);

		Key<?> key = mapper.getKey(className);
		assertNotNull("key for " + className + " cannot be null", key);

		Object instance2 = injector.getInstance(key);
		assertNotNull("instance of " + className + " fetched by type cannot be null", instance2);

		assertEquals("Expected same class, never mind the method", instance1.getClass(), instance2.getClass());
	}
}
