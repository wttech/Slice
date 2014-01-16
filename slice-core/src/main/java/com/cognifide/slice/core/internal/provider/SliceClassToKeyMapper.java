/*-
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
package com.cognifide.slice.core.internal.provider;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.sling.commons.classloader.DynamicClassLoaderManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.slice.api.provider.ClassToKeyMapper;
import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class SliceClassToKeyMapper implements ClassToKeyMapper {

	// cache - pre-filled with
	private final Map<String, Key<?>> knownKeys = new HashMap<String, Key<?>>();

	// Provider to ensure that we have a valid OSGi service in this singleton object
	private final Provider<DynamicClassLoaderManager> classLoaderManagerProvider;

	private static final Logger LOG = LoggerFactory.getLogger(SliceClassToKeyMapper.class);

	@Inject
	public SliceClassToKeyMapper(Injector injector,
			Provider<DynamicClassLoaderManager> classLoaderManagerProvider) {
		this.classLoaderManagerProvider = classLoaderManagerProvider;
		initiateKnownBindings(injector);
	}

	private void initiateKnownBindings(Injector injector) {
		final Map<Key<?>, Binding<?>> knownBindings = injector.getBindings();
		for (final Entry<Key<?>, Binding<?>> binding : knownBindings.entrySet()) {
			final Key<?> key = binding.getKey();
			if (key.getAnnotation() == null) {
				Class<?> clazz = key.getTypeLiteral().getRawType();
				knownKeys.put(clazz.getCanonicalName(), key);
			}
		}
	}

	@Override
	public Key<?> getKey(final String className) {
		Key<?> knownKey = knownKeys.get(className);
		if (knownKey == null) {
			try {
				Class<?> clazz = classLoaderManagerProvider.get().getDynamicClassLoader()
						.loadClass(className);
				knownKey = Key.get(clazz);
				// adding binding
				knownKeys.put(className, knownKey);
			} catch (ClassNotFoundException e) {
				String msg = "Unable to map class [{0}]";
				LOG.error(MessageFormat.format(msg, className), e);
				// returning null
			}
		}
		return knownKey;
	}
}
