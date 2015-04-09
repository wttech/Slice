package com.cognifide.slice.core.internal.scanner;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Mariusz Kubis
 */
public class BundleClassesFinderTest {

	private BundleClassesFinder classFinder;

	@Before
	public void setUp() throws ClassNotFoundException {
		classFinder = new BundleClassesFinder("test"){
			@Override
			@SuppressWarnings("unchecked")
			public Collection<Class<?>> getClasses(Collection<Bundle> bundles) {
				return new ArrayList<Class<?>>(Arrays.asList(TestService.class, TestService.InnerClass.class));
			}
		};
	}

	@Test
	public void testTraverseBundlesForOsgiServices() {
		Collection<Class<?>> classes = classFinder.traverseBundlesForOsgiServices(new ArrayList<Bundle>());
		Assert.assertEquals(1, classes.size());
		Assert.assertTrue(classes.contains(Integer.class));
	}
}