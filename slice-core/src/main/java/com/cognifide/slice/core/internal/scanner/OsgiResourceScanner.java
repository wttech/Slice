package com.cognifide.slice.core.internal.scanner;

import com.cognifide.slice.core.internal.module.AnnotationReader;
import com.cognifide.slice.mapper.annotation.SliceResource;
import org.objectweb.asm.ClassReader;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Jaromir Celejewski
 * @class OsgiResourceScanner
 * <p/>
 * Helper class for scanning bundles
 */
public class OsgiResourceScanner {
	private final BundleContext bundleContext;

	public OsgiResourceScanner(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	/**
	 * Returns collection of all classes that match given bundle filter and package.
	 */
	public Collection<Class<?>> findResources(String bundleNameFilter, String basePackage) {
		List<Bundle> bundles = findBundles(bundleNameFilter);
		BundleClassesFinder classFinder = new BundleClassesFinder(bundles, basePackage);
		return classFinder.traverseBundlesForOsgiServices();
	}

	private List<Bundle> findBundles(String bundleNameFilter) {
		Pattern bundleNamePattern = Pattern.compile(bundleNameFilter);
		List<Bundle> bundles = new ArrayList<Bundle>();
		for (Bundle bundle : bundleContext.getBundles()) {
			if (bundleNamePattern.matcher(bundle.getSymbolicName()).matches()) {
				bundles.add(bundle);
			}
		}
		return bundles;
	}
}
