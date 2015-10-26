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

import static org.junit.Assert.assertNotNull;

import java.util.Dictionary;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.apache.sling.testing.mock.sling.MockSling;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.cognifide.slice.persistence.api.ModelPersister;
import com.cognifide.slice.persistence.api.Serializer;
import com.cognifide.slice.persistence.api.SerializerFacade;
import com.cognifide.slice.persistence.impl.ModelPersisterService;
import com.cognifide.slice.persistence.impl.SerializerFacadeService;
import com.cognifide.slice.persistence.impl.serializer.ChildrenArraySerializer;
import com.cognifide.slice.persistence.impl.serializer.ChildrenCollectionSerializer;
import com.cognifide.slice.persistence.impl.serializer.EnumPropertySerializer;
import com.cognifide.slice.persistence.impl.serializer.NativeArraySerializer;
import com.cognifide.slice.persistence.impl.serializer.NativeCollectionSerializer;
import com.cognifide.slice.persistence.impl.serializer.NativePropertySerializer;
import com.cognifide.slice.persistence.impl.serializer.RecursiveSerializer;

public abstract class BaseTest {

	protected BundleContext bundleContext;

	protected ResourceResolver resolver;

	protected ModelPersister modelPersister;

	@SuppressWarnings("deprecation")
	@Before
	public void setup() throws LoginException {
		bundleContext = MockOsgi.newBundleContext();
		assertNotNull(bundleContext);

		register(bundleContext, SerializerFacade.class, new SerializerFacadeService());

		register(bundleContext, Serializer.class, new ChildrenArraySerializer());
		register(bundleContext, Serializer.class, new ChildrenCollectionSerializer());
		register(bundleContext, Serializer.class, new EnumPropertySerializer());
		register(bundleContext, Serializer.class, new NativeArraySerializer());
		register(bundleContext, Serializer.class, new NativeCollectionSerializer());
		register(bundleContext, Serializer.class, new NativePropertySerializer());
		register(bundleContext, Serializer.class, new RecursiveSerializer());

		register(bundleContext, ModelPersister.class, new ModelPersisterService());

		resolver = MockSling.newResourceResolverFactory(ResourceResolverType.JCR_MOCK, bundleContext)
				.getAdministrativeResourceResolver(null);
		assertNotNull(resolver);

		ServiceReference modelPersisterReference = bundleContext
				.getServiceReference(ModelPersister.class.getName());
		assertNotNull(modelPersisterReference);

		modelPersister = (ModelPersister) bundleContext.getService(modelPersisterReference);
		assertNotNull(modelPersister);
	}

	private void register(BundleContext bundleContext, Class<?> clazz, Object service) {
		MockOsgi.injectServices(service, bundleContext);
		MockOsgi.activate(service, bundleContext, (Dictionary<String, Object>) null);
		bundleContext.registerService(clazz.getName(), service, null);
	}
}
