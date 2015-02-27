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

	private Map<String, Map<String, ServiceRegistration>> registrationByInjectorAndBundle;

	public AdapterFactoryRegistry() {
		this.registrationByInjectorAndBundle = new HashMap<String, Map<String, ServiceRegistration>>();
	}

	public void addAdapter(ServiceRegistration registration, String injectorName, String bundleSymbolicName) {
		Map<String, ServiceRegistration> bundleRegistrations = registrationByInjectorAndBundle
				.get(injectorName);
		if (null == bundleRegistrations) {
			bundleRegistrations = new HashMap<String, ServiceRegistration>();
			registrationByInjectorAndBundle.put(injectorName, bundleRegistrations);
		}
		LOG.info("Adding AdapterFactory, injector:{}, bundle:{}", injectorName, bundleSymbolicName);

		bundleRegistrations.put(bundleSymbolicName, registration);
	}

	public void clearAll() {
		for (Map<String, ServiceRegistration> registrationByBundle : registrationByInjectorAndBundle.values()) {
			for (ServiceRegistration registration : registrationByBundle.values()) {
				registration.unregister();
			}
		}
		registrationByInjectorAndBundle.clear();
	}

	public void clearInjectorAdapters(String injectorName) {
		Map<String, ServiceRegistration> registrationByBundle = registrationByInjectorAndBundle
				.remove(injectorName);
		if (registrationByBundle != null) {
			for (ServiceRegistration registration : registrationByBundle.values()) {
				LOG.info("Removing AdapterFactory instances for injector, injector:{}", injectorName);

				registration.unregister();
			}
		}
	}

	public void clearBundleAdapter(String injectorName, String bundleSymbolicName) {
		Map<String, ServiceRegistration> registrationByBundle = registrationByInjectorAndBundle
				.get(injectorName);
		if (registrationByBundle != null) {
			ServiceRegistration registration = registrationByBundle.remove(bundleSymbolicName);
			if (registration != null) {
				LOG.info("Removing AdapterFactory for specific injector and bundle, injector:{}, bundle:{}",
						injectorName, bundleSymbolicName);

				registration.unregister();
			}
		}
	}
}
