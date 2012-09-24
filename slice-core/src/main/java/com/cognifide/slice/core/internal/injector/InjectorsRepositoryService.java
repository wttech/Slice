package com.cognifide.slice.core.internal.injector;

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

import org.apache.felix.scr.annotations.Component;

import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.Constants;

import com.cognifide.slice.api.injector.InjectorWithContext;
import com.cognifide.slice.api.injector.InjectorsRepository;
import com.cognifide.slice.api.qualifier.InjectorName;
import com.google.inject.Injector;
import com.google.inject.Key;

// @formatter:off
/**
 * @author Witold Szczerba
 * @author Rafał Malinowski
 * @class InjectorsRepositoryService
 */
@Component(immediate = true)
@Service
@Properties({ //
@Property(name = Constants.SERVICE_DESCRIPTION, value = "Repository of all slice injectors."), //
		@Property(name = Constants.SERVICE_VENDOR, value = "Cognifide") })
// @formatter:on
public final class InjectorsRepositoryService implements InjectorsRepository {

	@Reference(cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, referenceInterface = Injector.class, policy = ReferencePolicy.DYNAMIC)
	private final Map<String, Injector> injectors = new HashMap<String, Injector>();

	public InjectorWithContext getInjector(final String injectorName) {
		if (injectors.containsKey(injectorName)) {
			return new InjectorWithContextImpl(injectors.get(injectorName));
		} else {
			return null;
		}
	}

	protected void bindInjectors(final Injector injector) {
		final String injectorName = injector.getInstance(Key.get(String.class, InjectorName.class));
		injectors.put(injectorName, injector);
	}

	protected void unbindInjectors(final Injector injector) {
		final String injectorName = injector.getInstance(Key.get(String.class, InjectorName.class));
		injectors.remove(injectorName);
	}

}
