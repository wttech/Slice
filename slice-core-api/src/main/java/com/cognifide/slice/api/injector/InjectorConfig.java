package com.cognifide.slice.api.injector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Module;

public class InjectorConfig {
	private final List<Module> modules;

	private final String name;

	private final String parentName;

	InjectorConfig(InjectorRunner runner) {
		// we don't allow to change the module list after creating the configuration
		modules = Collections.unmodifiableList(new ArrayList<Module>(runner.getModules()));
		name = runner.getInjectorName();
		parentName = runner.getParentName();
	}

	public String getName() {
		return name;
	}

	public String getParentName() {
		return parentName;
	}

	public boolean hasParent() {
		return StringUtils.isNotBlank(parentName);
	}

	public List<Module> getModules() {
		return modules;
	}
}
