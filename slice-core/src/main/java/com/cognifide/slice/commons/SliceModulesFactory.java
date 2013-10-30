package com.cognifide.slice.commons;

import java.util.ArrayList;
import java.util.List;

import org.ops4j.peaberry.Peaberry;
import org.osgi.framework.BundleContext;

import com.cognifide.slice.api.context.ContextScope;
import com.cognifide.slice.core.internal.context.SliceContextScope;
import com.cognifide.slice.core.internal.module.JcrModule;
import com.cognifide.slice.core.internal.module.LinkModule;
import com.cognifide.slice.core.internal.module.SliceModule;
import com.cognifide.slice.core.internal.module.SliceResourceModule;
import com.cognifide.slice.core.internal.module.SlingModule;
import com.google.inject.Module;

/**
 * Factory for all Slice-related modules. It should be used in application's activator to register
 * Slice-related modules
 * 
 * @author maciej.majchrzak
 * 
 */
public class SliceModulesFactory {

	private SliceModulesFactory() {
	}

	/**
	 * Creates and returns a list of all Slice-related modules, including framework's internal modules as well
	 * as Sling's and JCR's ones. The list includes:<br>
	 * <ul>
	 * <li>Peaberry module</li>
	 * <li>{@link SliceModule}</li>
	 * <li>{@link SlingModule}</li>
	 * <li>{@link JcrModule}</li>
	 * <li>{@link LinkModule}</li>
	 * <li>{@link ContextModule}</li>
	 * <li>{@link SliceResourceModule}</li>
	 * </ul>
	 * 
	 * @return list of Slice-related modules
	 */
	public static List<Module> createModules(final BundleContext bundleContext, final String injectorName,
			final String bundleNameFilter, final String basePackage) {
		List<Module> modules = createCoreModules(bundleContext);
		modules.add(createSliceResourceModule(bundleContext, bundleNameFilter, basePackage));
		return modules;
	}

	public static Module createSliceResourceModule(final BundleContext bundleContext,
			final String bundleNameFilter, final String basePackage) {
		return new SliceResourceModule(bundleContext, bundleNameFilter, basePackage);
	}

	public static List<Module> createCoreModules(final BundleContext bundleContext) {
		final ContextScope contextScope = new SliceContextScope();
		List<Module> modules = new ArrayList<Module>();
		modules.add(Peaberry.osgiModule(bundleContext));
		modules.add(new SliceModule(contextScope, bundleContext.getBundle()));
		modules.add(new SlingModule(contextScope));
		modules.add(new JcrModule());
		modules.add(new LinkModule());
		return modules;
	}
}
