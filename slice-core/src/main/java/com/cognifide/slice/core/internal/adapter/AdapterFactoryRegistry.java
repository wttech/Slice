package com.cognifide.slice.core.internal.adapter;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.ServiceRegistration;

public class AdapterFactoryRegistry {

	private Map<String, Map<String, ServiceRegistration>> registrationByInjectorAndBundle;

	public AdapterFactoryRegistry() {
		this.registrationByInjectorAndBundle = new HashMap<String, Map<String,ServiceRegistration>>();
	}

	public void addAdapter(ServiceRegistration registration, String injectorName, String bundleSymbolicName) {
		Map<String, ServiceRegistration> bundleRegistrations = registrationByInjectorAndBundle
				.get(injectorName);
		if (null == bundleRegistrations) {
			bundleRegistrations = new HashMap<String, ServiceRegistration>();
			registrationByInjectorAndBundle.put(injectorName, bundleRegistrations);
		}
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
		Map<String, ServiceRegistration> registrationByBundle = registrationByInjectorAndBundle.remove(injectorName);
		if (registrationByBundle != null) {
			for (ServiceRegistration registration : registrationByBundle.values()) {
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
				registration.unregister();
			}
		}
	}
}
