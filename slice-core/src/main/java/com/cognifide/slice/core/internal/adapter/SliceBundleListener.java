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

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.api.injector.InjectorConfig;
import com.cognifide.slice.core.internal.injector.InjectorLifecycleListener;
import com.google.inject.Injector;

@Component
@Service(value = InjectorLifecycleListener.class)
public class SliceBundleListener implements BundleListener, InjectorLifecycleListener{

	private static final Logger LOG = LoggerFactory.getLogger(SliceBundleListener.class);

	private BundleContext bundleContext;
	
	private Set<InjectorConfig> configs = new HashSet<InjectorConfig>();

	@Activate
	public void activate(ComponentContext componentContext) {
		bundleContext = componentContext.getBundleContext();
		bundleContext.addBundleListener(this);
	}

	@Deactivate
	public void deactivate() {
		bundleContext.removeBundleListener(this);
	}

	@Override
	public void bundleChanged(BundleEvent event) {
		LOG.info("Bundle event!!!");
		for (final InjectorConfig config : configs) {
			if (bundleMatches(config.getBundleNameFilter(), event.getBundle())) {
				LOG.info("Matching bundle: " + event.getBundle().getSymbolicName());
			}
		}
	}
	
	@Override
	public void injectorCreated(Injector injector, InjectorConfig config) {
		configs.add(config);
	}
	
	@Override
	public void injectorDestroyed(Injector injector, InjectorConfig config) {
		configs.remove(config);
	}
	
	private boolean bundleMatches(String bundleNameFilter, Bundle bundle) {
		final Pattern bundleNamePattern = Pattern.compile(bundleNameFilter);
		return bundleNamePattern.matcher(bundle.getSymbolicName()).matches();
	}

}
