package com.cognifide.slice.core.internal.module;

/*
 * #%L
 * Slice - Core
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


import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.ops4j.peaberry.Peaberry;
import org.osgi.framework.Constants;

import com.cognifide.slice.api.injector.InjectorServiceRunner;
import com.cognifide.slice.api.module.SliceModulesInstaller;
import com.cognifide.slice.core.internal.module.JcrModule;
import com.cognifide.slice.core.internal.module.LinkModule;
import com.cognifide.slice.core.internal.module.SliceModule;
import com.cognifide.slice.core.internal.module.SliceResourceModule;
import com.cognifide.slice.core.internal.module.SlingModule;

// @formatter:off
/**
 * @author Rafał Malinowski
 * @class SliceModulesFactory
 */
@Component(immediate = true)
@Service
@Properties({ //
@Property(name = Constants.SERVICE_DESCRIPTION, value = "Factory of Slice modules."), //
		@Property(name = Constants.SERVICE_VENDOR, value = "Cognifide") })
// @formatter:on
public final class SliceModulesInstallerImpl implements SliceModulesInstaller {

	@Override
	public void installSliceModules(final InjectorServiceRunner injectorServiceRunner) {
		injectorServiceRunner.installModule(Peaberry.osgiModule(injectorServiceRunner.getBundleContext()));
		injectorServiceRunner.installModule(new SliceModule(injectorServiceRunner.getInjectorName(),
				injectorServiceRunner.getContextScope()));
		injectorServiceRunner.installModule(new SlingModule(injectorServiceRunner.getContextScope()));
		injectorServiceRunner.installModule(new JcrModule());
		injectorServiceRunner.installModule(new LinkModule());
		injectorServiceRunner.installModule(new ContextModule());
	}

	@Override
	public void installSliceResourceModule(InjectorServiceRunner injectorServiceRunner,
			String bundleNameFilter, String basePackage) {
		injectorServiceRunner.installModule(new SliceResourceModule(injectorServiceRunner.getBundleContext(),
				bundleNameFilter, basePackage));
	}

}
