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

import com.google.inject.Module;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.LoggerFactory;

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

	private ServiceRegistration registration;

	private boolean injectorCreationSuccess = true;

	/**
	 * @param bundleContext Context used to get access to the OSGi
	 * @param injectorName  Name of the new injector
	 */
	public InjectorRunner(final BundleContext bundleContext, final String injectorName,
			final String applicationPath, final String bundleNameFilter, final String basePackage) {
		this(bundleContext, injectorName, bundleNameFilter, basePackage);
		this.applicationPath = applicationPath;
	}

	/**
	 * @param bundleContext Context used to get access to the OSGi
	 * @param injectorName  Name of the new injector
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
		final InjectorConfig config = new InjectorConfig(this);

		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		registration = bundleContext.registerService(InjectorConfig.class.getName(), config, properties);

		//In some situations InjectorCreationFailListener.creationFailed() is called synchronously inside
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
