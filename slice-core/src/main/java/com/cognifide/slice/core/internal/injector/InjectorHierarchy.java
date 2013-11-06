/*-
 * #%L
 * Slice - Core API
 * $Id:$
 * $HeadURL:$
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
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.api.injector.InjectorConfig;
import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class InjectorHierarchy {

	private static final Logger LOG = LoggerFactory.getLogger(InjectorHierarchy.class);

	private Map<String, Injector> injectorByName = new HashMap<String, Injector>();

	private Map<String, InjectorConfig> configByName = new HashMap<String, InjectorConfig>();

	private volatile Map<Injector, String> nameLookupMap = new HashMap<Injector, String>();

	public synchronized void registerInjector(InjectorConfig config) {
		configByName.put(config.getName(), config);

		List<InjectorConfig> injectorsToRefresh = getSubtree(config);
		refreshInjectors(injectorsToRefresh);
		refreshNameLookupMap();
	}

	public synchronized void unregisterInjector(InjectorConfig config) {
		List<InjectorConfig> injectorsToRemove = getSubtree(config);
		for (InjectorConfig c : injectorsToRemove) {
			injectorByName.remove(c.getName());
		}
		configByName.remove(config.getName());
		refreshNameLookupMap();
	}

	public synchronized Injector getInjectorByName(String injectorName) {
		return injectorByName.get(injectorName);
	}

	public String getRegisteredName(Injector injector) {
		return nameLookupMap.get(injector);
	}

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
				throw new IllegalStateException("There is a cycle in the injectors graph: "
						+ flatSubtree.toString());
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
			return Guice.createInjector(modules);
		} catch (CreationException e) {
			LOG.error("Can't create injector " + config.getName(), e);
			return null;
		}
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
}
