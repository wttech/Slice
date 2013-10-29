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
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class InjectorHierarchy {

	private static final Logger LOG = LoggerFactory.getLogger(InjectorHierarchy.class);

	private Map<String, Injector> injectorByName = new HashMap<String, Injector>();

	private Map<String, InjectorConfig> configByName = new HashMap<String, InjectorConfig>();

	public synchronized void registerInjector(InjectorConfig config) {
		configByName.put(config.getName(), config);

		List<InjectorConfig> injectorsToRefresh = getSubtree(config);
		refreshInjectors(injectorsToRefresh);
	}

	public synchronized void unregisterInjector(InjectorConfig config) {
		List<InjectorConfig> injectorsToRemove = getSubtree(config);
		for (InjectorConfig c : injectorsToRemove) {
			injectorByName.remove(c.getName());
		}
		configByName.remove(config.getName());
	}

	public synchronized Injector getInjectorByName(String injectorName) {
		return injectorByName.get(injectorName);
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
				parent = configByName.get(current.getParentName());
				if (parent == null) {
					LOG.info("Can't create " + config.getName() + " as its ancestor " + current.getParentName()
							+ " can't be found");
					return null;
				}
			}
			current = parent;
		} while (current != null);
		return Guice.createInjector(modules);
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

	public synchronized String getName(Injector injector) {
		for (Entry<String, Injector> entry : injectorByName.entrySet()) {
			if (entry.getValue() == injector) {
				return entry.getKey();
			}
		}
		return null;
	}

	public Collection<String> getNames() {
		return injectorByName.keySet();
	}
}
