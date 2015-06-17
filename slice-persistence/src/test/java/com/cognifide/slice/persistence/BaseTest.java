/*-
 * #%L
 * Slice - Persistence
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
package com.cognifide.slice.persistence;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.MockSling;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;

import com.cognifide.slice.persistence.impl.SlicePersistenceService;
import com.cognifide.slice.persistence.module.PersistenceModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public abstract class BaseTest {

	protected ResourceResolver resolver;

	protected SlicePersistenceService persistence;

	@Before
	public void setup() throws LoginException {
		resolver = MockSling.newResourceResolverFactory(ResourceResolverType.JCR_MOCK)
				.getAdministrativeResourceResolver(null);
		Injector injector = Guice.createInjector(new PersistenceModule());
		persistence = injector.getInstance(SlicePersistenceService.class);
	}
}
