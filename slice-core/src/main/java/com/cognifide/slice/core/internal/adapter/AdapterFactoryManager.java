/*-
 * #%L
 * Slice - Core
 * %%
 * Copyright (C) 2012 Wunderman Thompson Technology
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
import java.util.Map.Entry;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.adapter.AdapterFactory;
import org.apache.sling.api.resource.Resource;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.api.injector.InjectorConfig;
import com.cognifide.slice.core.internal.injector.InjectorLifecycleListener;
import com.cognifide.slice.core.internal.scanner.BundleFinder;
import com.cognifide.slice.core.internal.scanner.BundleInfo;
import com.cognifide.slice.core.internal.scanner.BundleMatcher;
import com.cognifide.slice.core.internal.scanner.SliceResourceScanner;
import com.google.inject.Injector;

/**
 * This component is responsible for creation and destruction of the AdapterFactory services. Every time a
 * injector is created, it looks for all classes annotated with SliceResource annotation and creates a
 * separate AdapterFactory for each bundle containing matching classes.<br>
 * Each time a bundle is restarted, corresponding AdapterFactory is updated with the most recent list of
 * adapters.
 * 
 * @author Tomasz Rękawek
 * @author Dominik Kornaś
 * 
 */
@Component(immediate = true)
@Service(value = InjectorLifecycleListener.class)
public class AdapterFactoryManager implements InjectorLifecycleListener, BundleListener {

	private static final Logger LOG = LoggerFactory.getLogger(AdapterFactoryManager.class);

	private AdapterFactoryRegistry registry;

	private Map<String, BundleMatcher> matchersByInjector;

	private BundleContext bundleContext;

	private SliceResourceScanner scanner;

	@Activate
	public void activate(ComponentContext componentContext) {
		bundleContext = componentContext.getBundleContext();
		registry = new AdapterFactoryRegistry();
		matchersByInjector = new HashMap<String, BundleMatcher>();
		scanner = new SliceResourceScanner();

		bundleContext.addBundleListener(this);
	}

	@Deactivate
	public void deactivate() {
		bundleContext.removeBundleListener(this);

		registry.clearAll();
		matchersByInjector.clear();
	}

	@Override
	public void injectorCreated(Injector injector, InjectorConfig config) {
		final BundleInfo bundleInfo = new BundleInfo(config.getBundleNameFilter(), config.getBasePackage());
		final BundleFinder bundleFinder = new BundleFinder(bundleInfo, bundleContext);
		for (Bundle bundle : bundleFinder.findBundles()) {
			final Collection<Class<?>> classes = scanner.findSliceResources(bundle, config.getBasePackage());
			if (classes.isEmpty()) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("No classes found, skipping creation of AdapterFactory.");
				}
				continue;
			}
			final ServiceRegistration registration = createAdapterFactory(classes, config.getName());
			registry.addAdapter(config.getName(), bundle.getSymbolicName(), registration);
		}
		matchersByInjector.put(config.getName(), new BundleMatcher(bundleInfo));
	}

	@Override
	public void injectorDestroyed(Injector injector, InjectorConfig config) {
		registry.clearInjectorAdapters(config.getName());
		matchersByInjector.remove(config.getName());
	}

	@Override
	public void bundleChanged(BundleEvent event) {
		if (event.getType() != BundleEvent.RESOLVED && event.getType() != BundleEvent.UNRESOLVED) {
			// other event type; don't handle
			return;
		}

		final Bundle bundle = event.getBundle();
		for (Entry<String, BundleMatcher> entry : matchersByInjector.entrySet()) {
			String injectorName = entry.getKey();
			BundleMatcher matcher = entry.getValue();
			if (entry.getValue().matches(bundle.getSymbolicName())) {
				if (event.getType() == BundleEvent.RESOLVED) {
					updateAdapterFactory(injectorName, bundle, matcher.getBundleInfo().getBasePackage());
					return;
				} else if (event.getType() == BundleEvent.UNRESOLVED) {
					registry.clearBundleAdapter(injectorName, bundle.getSymbolicName());
					return;
				}
			}
		}
	}

	private ServiceRegistration createAdapterFactory(Collection<Class<?>> classes, String name) {
		String[] adapterClassNames = getClassNames(classes);
		SliceAdapterFactory factory = new SliceAdapterFactory(name);

		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put(AdapterFactory.ADAPTABLE_CLASSES, new String[] { Resource.class.getName() });
		properties.put(AdapterFactory.ADAPTER_CLASSES, adapterClassNames);
		return bundleContext.registerService(AdapterFactory.class.getName(), factory, properties);
	}

	private void updateAdapterFactory(final String injectorName, final Bundle bundle, final String basePackage) {
		registry.clearBundleAdapter(injectorName, bundle.getSymbolicName());

		final Collection<Class<?>> classes = scanner.findSliceResources(bundle, basePackage);
		if (!classes.isEmpty()) {
			ServiceRegistration newRegistration = createAdapterFactory(classes, injectorName);
			registry.addAdapter(injectorName, bundle.getSymbolicName(), newRegistration);
		} else if (LOG.isDebugEnabled()) {
			LOG.debug("No classes found, skipping creation of AdapterFactory.");
		}
	}

	private String[] getClassNames(Collection<Class<?>> classes) {
		String[] classNames = new String[classes.size()];
		int i = 0;
		for (Class<?> clazz : classes) {
			classNames[i++] = clazz.getName();
		}

		return classNames;
	}
}
