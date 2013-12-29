package com.cognifide.slice.core.internal.adapter;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.adapter.AdapterFactory;
import org.apache.sling.api.resource.Resource;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.api.context.RequestContextProvider;
import com.cognifide.slice.api.injector.InjectorConfig;
import com.cognifide.slice.api.injector.InjectorsRepository;
import com.cognifide.slice.core.internal.injector.InjectorListener;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * This component is responsible for creation and destruction of the AdapterFactory services. Every time a
 * injector is created, it looks for all modules implementing AdaptableModule interface and creates a separate
 * AdapterFactory for each module it founds.
 * 
 * @author Tomasz Rękawek
 * 
 */
@Component
@Service
public class AdapterFactoryManager implements InjectorListener {

	private static final Logger LOG = LoggerFactory.getLogger(AdapterFactoryManager.class);

	@Reference
	private InjectorsRepository repository;

	@Reference
	private RequestContextProvider requestContextProvider;

	private Map<AdaptableModule, List<String>> modules;

	private Map<AdaptableModule, ServiceRegistration> registrationByModule;

	private BundleContext bundleContext;

	@Activate
	public void activate(ComponentContext componentContext) {
		bundleContext = componentContext.getBundleContext();
		modules = new HashMap<AdaptableModule, List<String>>();
		registrationByModule = new HashMap<AdaptableModule, ServiceRegistration>();
	}

	@Override
	public void injectorCreated(Injector injector, InjectorConfig config) {
		for (Module m : config.getModules()) {
			if (m instanceof AdaptableModule) {
				registerModule((AdaptableModule) m, config.getName());
			}
		}
	}

	@Override
	public void injectorDestroyed(Injector injector, InjectorConfig config) {
		for (Module m : config.getModules()) {
			if (m instanceof AdaptableModule) {
				unregisterModule((AdaptableModule) m, config.getName());
			}
		}
	}

	private void registerModule(AdaptableModule module, String name) {
		if (!modules.containsKey(module)) {
			modules.put(module, new ArrayList<String>());
			createAdapterFactory(module, name);
		}
		modules.get(module).add(name);
	}

	private void unregisterModule(AdaptableModule module, String name) {
		List<String> injectors = modules.get(module);
		if (injectors == null) {
			LOG.error("Can't unregister nonexisting module {} for injector {}", new Object[] { module, name });
			return;
		}
		injectors.remove(name);
		if (injectors.isEmpty()) {
			destroyAdapterFactory(module);
			modules.remove(module);
		}
	}

	private void createAdapterFactory(AdaptableModule module, String name) {
		String[] adapterClasses = new String[module.getClasses().size()];
		int i = 0;
		for (Class<?> clazz : module.getClasses()) {
			adapterClasses[i++] = clazz.getName();
		}

		SliceAdapterFactory factory = new SliceAdapterFactory(name, repository, requestContextProvider);
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put(AdapterFactory.ADAPTABLE_CLASSES, new String[] { Resource.class.getName() });
		properties.put(AdapterFactory.ADAPTER_CLASSES, adapterClasses);
		ServiceRegistration registration = bundleContext.registerService(AdapterFactory.class.getName(),
				factory, properties);

		registrationByModule.put(module, registration);
	}

	private void destroyAdapterFactory(AdaptableModule module) {
		ServiceRegistration registration = registrationByModule.remove(module);
		if (registration != null) {
			registration.unregister();
		}
	}
}
