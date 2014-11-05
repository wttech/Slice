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

import java.util.IdentityHashMap;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.cognifide.slice.api.injector.InjectorConfig;
import com.cognifide.slice.api.injector.InjectorListener;
import com.google.inject.Injector;

/**
 * This component bridges each registered InjectorListener with a newly created
 * {@link InjectorLifecycleListener}. The first listener type is a part of the public API and can be used in
 * the client application, where the second is meant to be used internally, as it receives more information,
 * including some implementation details like {@link InjectorConfig}. Therefore we need such component to
 * strip events passed to the {@link InjectorLifecycleListener} and inform {@link InjectorListener} only about
 * the name of the created injector.
 * 
 * @author Tomasz Rękawek
 *
 */
@Component
public class InjectorListenerWhiteboard {

	@Reference(cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, referenceInterface = InjectorListener.class, policy = ReferencePolicy.DYNAMIC)
	private final Map<InjectorListener, ServiceRegistration> listeners = new IdentityHashMap<InjectorListener, ServiceRegistration>();

	private BundleContext context;

	public void activate(BundleContext context) {
		this.context = context;
	}

	protected void bindListeners(final InjectorListener listener) {
		final InjectorLifecycleListener lifecycleListener = new InjectorListenerBridge(listener);
		final ServiceRegistration registration = context.registerService(
				InjectorLifecycleListener.class.getName(), lifecycleListener, null);
		listeners.put(listener, registration);
	}

	protected void unbindListeners(InjectorListener listener) {
		final ServiceRegistration registration = listeners.remove(listener);
		if (registration != null) {
			registration.unregister();
		}
	}

	private static class InjectorListenerBridge implements InjectorLifecycleListener {
		private final InjectorListener listener;

		private InjectorListenerBridge(InjectorListener listener) {
			this.listener = listener;
		}

		@Override
		public void injectorCreated(Injector injector, InjectorConfig config) {
			listener.injectorAvailable(config.getName());
		}

		@Override
		public void injectorDestroyed(Injector injector, InjectorConfig config) {
			// do nothing
		}

	}
}
