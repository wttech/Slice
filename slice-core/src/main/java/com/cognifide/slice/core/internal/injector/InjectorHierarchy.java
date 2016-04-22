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

package com.cognifide.slice.core.internal.injector;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.api.injector.InjectorConfig;
import com.cognifide.slice.core.internal.module.OsgiToGuiceAutoBindModule;
import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;

/**
 * This class stores injector configuration tree and creates injectors associated with these configurations.
 * 
 * @author Tomasz Rekawek
 */
@Component
@Service(value = InjectorHierarchy.class)
public class InjectorHierarchy {

	private static final Logger LOG = LoggerFactory.getLogger(InjectorHierarchy.class);

	@Reference(cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, referenceInterface = InjectorConfig.class, policy = ReferencePolicy.DYNAMIC, bind = "bindConfig", unbind = "unbindConfig")
	private final Map<String, InjectorConfig> configByName = new HashMap<String, InjectorConfig>();

	@Reference(cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, referenceInterface = InjectorLifecycleListener.class, policy = ReferencePolicy.DYNAMIC)
	private final Set<InjectorLifecycleListener> listeners = new HashSet<InjectorLifecycleListener>();

	private final Map<String, Injector> injectorByName = new HashMap<String, Injector>();

	private volatile Map<String, String> namesByPath = new HashMap<String, String>();

	private volatile Map<Injector, String> nameLookupMap = new HashMap<Injector, String>();

	@Deactivate
	public void deactivate() {
		configByName.clear();
		injectorByName.clear();
		nameLookupMap.clear();
		listeners.clear();
		namesByPath.clear();
	}

	/**
	 * Register new injector configuration. If all ancestors of the new config are already registered, method
	 * will create new injector. If this config is a missing ancestor of some other config, the child injector
	 * (or injectors) will be created as well.
	 * 
	 * @param config Injector configuration
	 */
	private synchronized void registerInjector(InjectorConfig config) {
		configByName.put(config.getName(), config);

		List<InjectorConfig> injectorsToRefresh = getSubtree(config);
		refreshInjectors(injectorsToRefresh);
		refreshNameLookupMap();
		refreshNamesByPathMap();
	}

	/**
	 * Unregister given injector config, destroy associated injector and all its children.
	 * 
	 * @param config
	 */
	private synchronized void unregisterInjector(InjectorConfig config) {
		List<InjectorConfig> injectorsToRemove = getSubtree(config);
		for (InjectorConfig c : injectorsToRemove) {
			Injector injector = injectorByName.remove(c.getName());
			for (InjectorLifecycleListener listener : listeners) {
				listener.injectorDestroyed(injector, c);
			}
		}
		configByName.remove(config.getName());
		refreshNameLookupMap();
		refreshNamesByPathMap();
	}

	/**
	 * Return injector with given name
	 * 
	 * @param injectorName Injector name
	 * @return Injector or null if there is no such injector
	 */
	public synchronized Injector getInjectorByName(String injectorName) {
		return injectorByName.get(injectorName);
	}

	/**
	 * Return name of injector with given path
	 * 
	 * @param applicationPath Injector path
	 * @return Injector name or null if there is no such injector
	 */
	public synchronized String getInjectorNameByApplicationPath(String applicationPath) {
		return namesByPath.get(applicationPath);
	}

	/**
	 * Return name of the given injector
	 * 
	 * @param injector Injector object
	 * @return Injector name or null if there is no such injector
	 */
	public String getRegisteredName(Injector injector) {
		return nameLookupMap.get(injector);
	}

	/**
	 * Return names of all created injectors
	 * 
	 * @return Collection with names of all successfully created injectors.
	 */
	public Collection<String> getInjectorNames() {
		return nameLookupMap.values();
	}

	private List<InjectorConfig> getSubtree(InjectorConfig root) {
		List<InjectorConfig> flatSubtree = new ArrayList<InjectorConfig>();
		Deque<InjectorConfig> queue = new ArrayDeque<InjectorConfig>();
		queue.add(root);

		while (!queue.isEmpty()) {
			InjectorConfig node = queue.poll();
			if (!flatSubtree.add(node)) {
				throw new IllegalStateException(
						"There is a cycle in the injectors graph: " + flatSubtree.toString());
			}
			queue.addAll(getChildren(node));
		}
		return flatSubtree;
	}

	private void refreshInjectors(List<InjectorConfig> injectors) {
		for (InjectorConfig config : injectors) {
			Injector injector = createInjector(config);
			if (injector != null) {
				injectorByName.put(config.getName(), injector);
			}
		}
	}

	private Injector createInjector(InjectorConfig config) {
		List<Module> modules = new ArrayList<Module>();
		InjectorConfig current = config;
		do {
			modules.addAll(current.getModules());
			InjectorConfig parent = null;
			if (current.hasParent()) {
				String parentName = current.getParentName();
				parent = configByName.get(parentName);
				if (parent == null) {
					LOG.info("Can't create {} as its ancestor {} can't be found",
							new Object[] { config.getName(), parentName });
					return null;
				}
			}
			current = parent;
		} while (current != null);
		try {
			Injector injector = Guice.createInjector(handleModuleOverrides(modules));
			for (InjectorLifecycleListener listener : listeners) {
				listener.injectorCreated(injector, config);
			}
			return injector;
		} catch (CreationException e) {
			LOG.error("Can't create injector " + config.getName(), e);
			config.getListener().creationFailed();
			return null;
		}
	}

	/**
	 * Overrides regular modules by {@link OsgiToGuiceAutoBindModule} if exists
	 *
	 * @param modules modules list
	 * @return overrated module list
	 */
	private List<Module> handleModuleOverrides(List<Module> modules) {
		List<Module> regulars = new ArrayList<Module>();
		List<Module> overrides = new ArrayList<Module>();
		for (Module module : modules) {
			if (module instanceof OsgiToGuiceAutoBindModule) {
				overrides.add(module);
			} else {
				regulars.add(module);
			}
		}
		List<Module> result = modules;
		if (!overrides.isEmpty()) {
			result = override(overrides, regulars);
		}
		return result;
	}

	private List<Module> override(List<Module> overrides, List<Module> regulars) {
		Collections.reverse(overrides);
		Module overridden = null;
		for (Module module : overrides) {
			if (overridden == null) {
				overridden = Modules.override(regulars).with(module);
			} else {
				overridden = Modules.override(overridden).with(module);
			}
		}
		return Collections.singletonList(overridden);
	}

	private Collection<InjectorConfig> getChildren(InjectorConfig parent) {
		List<InjectorConfig> children = new ArrayList<InjectorConfig>();
		String parentName = parent.getName();
		for (InjectorConfig child : configByName.values()) {
			if (parentName.equals(child.getParentName())) {
				children.add(child);
			}
		}
		return children;
	}

	private void refreshNameLookupMap() {
		Map<Injector, String> map = new HashMap<Injector, String>();
		for (Entry<String, Injector> entry : injectorByName.entrySet()) {
			map.put(entry.getValue(), entry.getKey());
		}
		nameLookupMap = map;
	}

	private void refreshNamesByPathMap() {
		Map<String, String> map = new HashMap<String, String>();
		for (Entry<String, InjectorConfig> entry : configByName.entrySet()) {
			map.put(entry.getValue().getApplicationPath(), entry.getKey());
		}
		namesByPath = map;
	}

	protected void bindConfig(final InjectorConfig config) {
		registerInjector(config);
	}

	protected void unbindConfig(final InjectorConfig config) {
		unregisterInjector(config);
	}

	protected void bindListeners(final InjectorLifecycleListener listener) {
		for (Entry<String, Injector> entry : injectorByName.entrySet()) {
			String name = entry.getKey();
			InjectorConfig config = configByName.get(name);
			listener.injectorCreated(entry.getValue(), config);
		}
		listeners.add(listener);
	}

	protected void unbindListeners(final InjectorLifecycleListener listener) {
		listeners.remove(listener);
	}
}
