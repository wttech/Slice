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

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.adapter.AdapterFactory;

import com.cognifide.slice.api.context.RequestContextProvider;
import com.cognifide.slice.api.injector.InjectorsRepository;

@Component
@Service
@Properties({
		@Property(name = AdapterFactory.ADAPTABLE_CLASSES, value = {
				"org.apache.sling.api.SlingHttpServletRequest",
				"org.apache.sling.api.resource.ResourceResolver" }),
		@Property(name = AdapterFactory.ADAPTER_CLASSES, value = {
				"com.cognifide.slice.api.injector.InjectorsRepository",
				"com.cognifide.slice.api.context.RequestContextProvider" }) })
public class InjectorRepositoryAdapterFactory implements AdapterFactory {

	@Reference
	private InjectorsRepository repository;

	@Reference
	private RequestContextProvider requestContextProvider;

	@SuppressWarnings("unchecked")
	@Override
	public <AdapterType> AdapterType getAdapter(Object adaptable, Class<AdapterType> type) {
		AdapterType result = null;
		if (type.isAssignableFrom(InjectorsRepository.class)) {
			result = (AdapterType) repository;
		} else if (type.isAssignableFrom(RequestContextProvider.class)) {
			result = (AdapterType) requestContextProvider;
		}
		return result;
	}

}
