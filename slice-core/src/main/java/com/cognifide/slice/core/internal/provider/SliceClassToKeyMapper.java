package com.cognifide.slice.core.internal.provider;

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


import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.cognifide.slice.api.provider.ClassToKeyMapper;
import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;

@Singleton
public class SliceClassToKeyMapper implements ClassToKeyMapper {

	private final Map<String, Key<?>> classToKeyMapping = new HashMap<String, Key<?>>();

	@Inject
	public SliceClassToKeyMapper(final Injector injector) {
		final Map<Key<?>, Binding<?>> knownBindings = injector.getBindings();
		for (final Entry<Key<?>, Binding<?>> binding : knownBindings.entrySet()) {
			final Key<?> key = binding.getKey();
			if (null == key.getAnnotation()) {
				classToKeyMapping.put(key.getTypeLiteral().getType().toString(), key);
			}
		}
	}

	@Override
	public Key<?> getKey(final String className) {
		return classToKeyMapping.get("class " + className);
	}

}
