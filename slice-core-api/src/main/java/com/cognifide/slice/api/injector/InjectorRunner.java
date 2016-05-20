/*-
 * #%L
 * Slice - Core API
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

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.slf4j.LoggerFactory;

import com.google.inject.Module;

/**
 * InjectorRunner is an entry point to configure and create injector in an application using Slice. It should
 * mainly be used in application activator (an implementation of {@link org.osgi.framework.BundleActivator})
 * Basically, one needs to create injector runner, add all needed modules ({@link #installModule(Module)
 * installModule} or {@link #installModules(List) installModules}) and start it ({@link #start() start})
 * 
 * Example:
 * 
 * <pre>
 * {@code public class Activator implements BundleActivator {
 * 
 * {@literal @}Override
 * public void start(BundleContext bundleContext) throws Exception {
 * 
 * 	final InjectorRunner injectorRunner = new InjectorRunner(bundleContext, "slice-test",
 * 	"slice-test-app.*", "com.cognifide.example");
 * 
 * 	List<Module> sliceModules = SliceModulesFactory.createModules(bundleContext);
 * 	List<Module> cqModules = CQModulesFactory.createModules();
 * 
 * 	injectorRunner.installModules(sliceModules);
 * 	injectorRunner.installModules(cqModules);
 * 	injectorRunner.installModule(...);
 * 
 * 	injectorRunner.start();
 * }
 * }
 * }
 * </pre>
 */
public class InjectorRunner implements InjectorCreationFailListener {

	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(InjectorRunner.class);

	private final String injectorName;

	private final List<Module> modules = new ArrayList<Module>();

	private final BundleContext bundleContext;

	private final String bundleNameFilter;

	private final String basePackage;

	private boolean started = false;

	private String parentInjectorName;

	private String applicationPath;

	private boolean injectorCreationSuccess = true;

	/**
	 * @param bundleContext Context used to get access to the OSGi
	 * @param injectorName Name of the new injector
	 * @param applicationPath path to the application, e.g. "/apps/myapp"
	 * @param bundleNameFilter filter used to scan bundles
	 * @param basePackage base package for classes to be scanned
	 */
	public InjectorRunner(final BundleContext bundleContext, final String injectorName,
			final String applicationPath, final String bundleNameFilter, final String basePackage) {
		this(bundleContext, injectorName, bundleNameFilter, basePackage);
		this.applicationPath = applicationPath;
	}

	/**
	 * @param bundleContext Context used to get access to the OSGi
	 * @param injectorName Name of the new injector
	 * @param bundleNameFilter filter used to scan bundles
	 * @param basePackage base package for classes to be scanned
	 */
	public InjectorRunner(final BundleContext bundleContext, final String injectorName,
			final String bundleNameFilter, final String basePackage) {
		this.bundleContext = bundleContext;
		this.injectorName = injectorName;
		this.bundleNameFilter = bundleNameFilter;
		this.basePackage = basePackage;
	}

	public void setParentInjectorName(String parentInjectorName) {
		this.parentInjectorName = parentInjectorName;
	}

	public void installModule(final Module newModule) {
		if (started) {
			throw new IllegalStateException(
					"Installing new modules is not allowed after Injector was stared");
		}
		modules.add(newModule);
	}

	public void installModules(final List<? extends Module> newModules) {
		if (started) {
			throw new IllegalStateException(
					"Installing new modules is not allowed after Injector was stared");
		}
		modules.addAll(newModules);
	}

	@Override
	public void creationFailed() {
		try {
			injectorCreationSuccess = false;
			started = false;
			bundleContext.getBundle().stop();
		} catch (BundleException e) {
			LOG.error("InjectorRunner failed to stop the bundle on injector creation failure", e);
		}
	}

	public void start() throws BundleException {
		modules.add(new InjectorNameModule(this));
		final InjectorConfig config = new InjectorConfig(this);

		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		bundleContext.registerService(InjectorConfig.class.getName(), config, properties);

		// In some situations InjectorCreationFailListener.creationFailed() is called synchronously inside
		// registerService() method. To prevent bundle activation in this case it is necessary to check
		// injector status here once more and eventually leave by calling BundleException
		if (injectorCreationSuccess) {
			started = true;
		} else {
			started = false;
			throw new BundleException("Failed to create an injector", BundleException.ACTIVATOR_ERROR);
		}
	}

	public String getInjectorName() {
		return injectorName;
	}

	String getBundleNameFilter() {
		return bundleNameFilter;
	}

	String getBasePackage() {
		return basePackage;
	}

	List<? extends Module> getModules() {
		return modules;
	}

	String getParentName() {
		return parentInjectorName;
	}

	String getApplicationPath() {
		return applicationPath;
	}
}
