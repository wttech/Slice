package com.cognifide.slice.core.internal.module;

import com.cognifide.slice.core.internal.scanner.OsgiResourceScanner;
import com.google.inject.AbstractModule;
import com.google.inject.binder.AnnotatedBindingBuilder;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.builders.DecoratedServiceBuilder;
import org.osgi.framework.BundleContext;

import java.util.Collection;

/**
 * @author Jaromir Celejewski
 * @class OsgiToGuiceAutoBindModule
 * <p/>
 * This module class will scan given bundles and automatically bind all OSGi services
 * marked with @OsgiService annotation using Peaberry. Instance of this module should be installed in
 * injector:
 * injectorRunner.installModule(new OsgiToGuiceAutoBindModule(bundleContext, bundle_name_filter, base_package));
 * Also @see com.cognifide.slice.annotations.OsgiService
 */
public class OsgiToGuiceAutoBindModule extends AbstractModule {
	private String bundleNameFilter;

	private String basePackage;

	private BundleContext bundleContext;

	public OsgiToGuiceAutoBindModule(BundleContext bundleContext, String bundleNameFilter,
			String basePackage) {
		this.bundleNameFilter = bundleNameFilter;
		this.basePackage = basePackage;
		this.bundleContext = bundleContext;
	}

	@Override protected void configure() {
		OsgiResourceScanner scanner = new OsgiResourceScanner(bundleContext);
		Collection<Class<?>> requestedOsgiClasses = scanner.findResources(bundleNameFilter, basePackage);
		for (Class<?> clazz : requestedOsgiClasses) {
			((AnnotatedBindingBuilder<Object>) bind(clazz)).toProvider(
					((DecoratedServiceBuilder<Object>) Peaberry.service(clazz)).single());
		}
	}
}
