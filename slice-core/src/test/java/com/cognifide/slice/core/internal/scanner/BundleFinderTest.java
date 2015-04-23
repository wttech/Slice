package com.cognifide.slice.core.internal.scanner;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import static org.mockito.Mockito.when;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class BundleFinderTest {

	@Mock
	private BundleContext bundleContext;

	@Mock
	private Bundle matchingBundle;

	@Mock
	private Bundle notMatchingBundle;

	private BundleInfo bundleInfo;

	@Before
	public void setUp() {
		bundleInfo = new BundleInfo("matchingFilter", "basePackage");
		when(bundleContext.getBundles()).thenReturn(new Bundle[] { matchingBundle, notMatchingBundle });
		when(matchingBundle.getSymbolicName()).thenReturn("matchingFilter");
		when(notMatchingBundle.getSymbolicName()).thenReturn("notMatchingFilter");
	}

	@Test
	public void testFindBundles() throws Exception {
		BundleFinder bundleFinder = new BundleFinder(bundleInfo, bundleContext);
		List<Bundle> bundles = bundleFinder.findBundles();
		Assert.assertEquals(bundles.size(), 1);
		Assert.assertEquals(bundles.get(0), matchingBundle);

	}
}