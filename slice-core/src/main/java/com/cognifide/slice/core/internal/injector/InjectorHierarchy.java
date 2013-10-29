package com.cognifide.slice.core.internal.injector;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.api.injector.InjectorConfig;
import com.google.inject.Guice;
import com.google.inject.Injector;

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
			Injector injector = null;
			if (config.hasParent()) {
				String parentName = config.getParentName();
				Injector parent = injectorByName.get(parentName);
				if (parent == null) {
					LOG.info("Can't create injector " + injector + " as its parent " + parentName
							+ " is not yet registered");
				} else {
					injector = parent.createChildInjector(config.getModules());
				}
			} else {
				injector = Guice.createInjector(config.getModules());
			}
			if (injector != null) {
				injectorByName.put(config.getName(), injector);
			}
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
}
