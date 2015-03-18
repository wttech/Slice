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

import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.adapter.AdapterFactory;
import org.apache.sling.api.resource.Resource;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;

import com.cognifide.slice.api.context.RequestContextProvider;
import com.cognifide.slice.api.injector.InjectorConfig;
import com.cognifide.slice.api.injector.InjectorsRepository;
import com.cognifide.slice.core.internal.injector.InjectorLifecycleListener;
import com.cognifide.slice.core.internal.scanner.SliceResourceScanner;
import com.google.inject.Injector;

/**
 * This component is responsible for creation and destruction of the AdapterFactory services. Every time a
 * injector is created, it looks for all modules implementing AdaptableModule interface and creates a separate
 * AdapterFactory for each module it founds.
 * 
 * @author Tomasz Rękawek
 * 
 */
@Component(immediate = true)
@Service
public class AdapterFactoryManager implements InjectorLifecycleListener {

	@Reference
	private RequestContextProvider requestContextProvider;

	private Map<String, ServiceRegistration> registrationByInjector;

	private BundleContext bundleContext;

	private SliceResourceScanner scanner;

	@Activate
	public void activate(ComponentContext componentContext) {
		bundleContext = componentContext.getBundleContext();
		registrationByInjector = new HashMap<String, ServiceRegistration>();
		scanner = new SliceResourceScanner(bundleContext);
	}

	@Deactivate
	public void deactivate() {
		for (ServiceRegistration adapterFactory : registrationByInjector.values()) {
			adapterFactory.unregister();
		}
	}

	@Override
	public void injectorCreated(Injector injector, InjectorConfig config) {
		Collection<Class<?>> classes = scanner.findSliceResources(config.getBundleNameFilter(),
				config.getBasePackage());
		ServiceRegistration registration = createAdapterFactory(classes, config.getName(), injector);
		registrationByInjector.put(config.getName(), registration);
	}

	@Override
	public void injectorDestroyed(Injector injector, InjectorConfig config) {
		ServiceRegistration registration = registrationByInjector.remove(config.getName());
		if (registration != null) {
			registration.unregister();
		}
	}

	private ServiceRegistration createAdapterFactory(Collection<Class<?>> classes, String name,Injector injector) {
		String[] adapterClasses = new String[classes.size()];
		int i = 0;
		for (Class<?> clazz : classes) {
			adapterClasses[i++] = clazz.getName();
		}

		SliceAdapterFactory factory = new SliceAdapterFactory(name, injector, requestContextProvider);
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put(AdapterFactory.ADAPTABLE_CLASSES, new String[] { Resource.class.getName() });
		properties.put(AdapterFactory.ADAPTER_CLASSES, adapterClasses);
		ServiceRegistration registration = bundleContext.registerService(AdapterFactory.class.getName(),
				factory, properties);

		return registration;
	}
}
