package com.cognifide.slice.commons.provider;

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

import org.apache.sling.api.resource.Resource;

import com.cognifide.slice.api.model.InitializableModel;
import com.cognifide.slice.api.qualifier.EmptyObject;
import com.cognifide.slice.core.internal.provider.CurrentResourceProvider;
import com.cognifide.slice.mapper.api.Mapper;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * Used internally.
 * 
 * @TODO: investigate using a request scoped cache (immutable objects etc...)
 */
public class SliceResourceProvider {

	private final Injector injector;

	private final CurrentResourceProvider currentResourceProvider;

	private final Mapper mapper;

	@Inject
	public SliceResourceProvider(final CurrentResourceProvider currentResourceProvider,
			final Injector injector, final Mapper mapper) {
		this.injector = injector;
		this.currentResourceProvider = currentResourceProvider;
		this.mapper = mapper;
	}

	public <T> T get(final Class<T> sliceResourceClass) {
		final Resource resource = currentResourceProvider.get();
		return get(sliceResourceClass, resource);
	}

	public <T> T get(final Class<T> sliceResourceClass, final Resource resource) {
		T instance = newInstance(sliceResourceClass);
		if (resource != null) {
			instance = mapper.get(resource, instance);
		}
		if (InitializableModel.class.isAssignableFrom(sliceResourceClass)) {
			InitializableModel initializableModel = (InitializableModel) instance;
			initializableModel.afterCreated();
		}
		return instance;
	}

	private <T> T newInstance(final Class<T> sliceResourceClass) {
		final T instance = injector.getInstance(Key.get(sliceResourceClass, EmptyObject.class));
		if (instance == null) {
			throw new IllegalStateException("Cannot create empty instance of " + sliceResourceClass
					+ ". It has not been registered properly or it's not a valid Guice object.");
		}
		return instance;
	}
}
