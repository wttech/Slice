package com.cognifide.slice.activator;

import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.cognifide.slice.api.injector.InjectorRunner;
import com.cognifide.slice.commons.SliceModulesFactory;
import com.cognifide.slice.validation.ValidationModulesFactory;
import com.google.inject.Module;

public class Activator implements BundleActivator {

	private static final String INJECTOR_NAME = "slice";

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		List<Module> sliceModules = SliceModulesFactory.createCoreModules(bundleContext);
		List<Module> validationModules = ValidationModulesFactory.createModules();

		new InjectorRunner(bundleContext, INJECTOR_NAME) //
				.installModules(sliceModules) //
				.installModules(validationModules) //
				.start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}
}
