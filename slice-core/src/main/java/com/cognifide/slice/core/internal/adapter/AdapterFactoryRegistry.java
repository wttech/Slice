/*-
 * #%L
 * Slice - Core
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
package com.cognifide.slice.core.internal.adapter;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdapterFactoryRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(AdapterFactoryRegistry.class);

	private Map<String, Map<String, ServiceRegistration>> adapterFactoriesByInjectorAndBundle;

	public AdapterFactoryRegistry() {
		this.adapterFactoriesByInjectorAndBundle = new HashMap<String, Map<String, ServiceRegistration>>();
	}

	public void addAdapter(String injectorName, String bundleSymbolicName,
			ServiceRegistration adapterFactoryService) {
		Map<String, ServiceRegistration> injectorAdapterFactories = adapterFactoriesByInjectorAndBundle
				.get(injectorName);
		if (injectorAdapterFactories == null) {
			injectorAdapterFactories = new HashMap<String, ServiceRegistration>();
			adapterFactoriesByInjectorAndBundle.put(injectorName, injectorAdapterFactories);
		}
		LOG.debug("Adding AdapterFactory, injector: {}, bundle: {}", injectorName, bundleSymbolicName);

		injectorAdapterFactories.put(bundleSymbolicName, adapterFactoryService);
	}

	public void clearAll() {
		LOG.debug("Removing all AdapterFactories");
		for (Map<String, ServiceRegistration> adapterFactories : adapterFactoriesByInjectorAndBundle.values()) {
			for (ServiceRegistration adapterFactoryService : adapterFactories.values()) {
				adapterFactoryService.unregister();
			}
		}
		adapterFactoriesByInjectorAndBundle.clear();
	}

	public void clearInjectorAdapters(String injectorName) {
		Map<String, ServiceRegistration> injectorAdapterFactories = adapterFactoriesByInjectorAndBundle
				.remove(injectorName);
		if (injectorAdapterFactories != null) {
			for (ServiceRegistration adapterFactoryService : injectorAdapterFactories.values()) {
				LOG.debug("Removing AdapterFactory instances for injector, injector: {}", injectorName);

				adapterFactoryService.unregister();
			}
		}
	}

	public void clearBundleAdapter(String injectorName, String bundleSymbolicName) {
		Map<String, ServiceRegistration> injectorAdapterFactories = adapterFactoriesByInjectorAndBundle
				.get(injectorName);
		if (injectorAdapterFactories != null) {
			ServiceRegistration adapterFactoryService = injectorAdapterFactories.remove(bundleSymbolicName);
			if (adapterFactoryService != null) {
				LOG.debug("Removing AdapterFactory for specific injector and bundle, injector:{}, bundle:{}",
						injectorName, bundleSymbolicName);

				adapterFactoryService.unregister();
			}
		}
	}
}
