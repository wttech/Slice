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
package com.cognifide.slice.persistence.impl.module;

import com.cognifide.slice.persistence.api.serializer.Serializer;
import com.cognifide.slice.persistence.impl.serializer.ChildrenArraySerializer;
import com.cognifide.slice.persistence.impl.serializer.ChildrenCollectionSerializer;
import com.cognifide.slice.persistence.impl.serializer.EnumPropertySerializer;
import com.cognifide.slice.persistence.impl.serializer.NativeArraySerializer;
import com.cognifide.slice.persistence.impl.serializer.NativeCollectionSerializer;
import com.cognifide.slice.persistence.impl.serializer.NativePropertySerializer;
import com.cognifide.slice.persistence.impl.serializer.RecursiveSerializer;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class PersistenceModule extends AbstractModule {

	@Override
	protected void configure() {
		Multibinder<Serializer> serializerBinder = Multibinder.newSetBinder(binder(), Serializer.class);
		serializerBinder.addBinding().to(ChildrenArraySerializer.class);
		serializerBinder.addBinding().to(ChildrenCollectionSerializer.class);
		serializerBinder.addBinding().to(EnumPropertySerializer.class);
		serializerBinder.addBinding().to(NativeArraySerializer.class);
		serializerBinder.addBinding().to(NativeCollectionSerializer.class);
		serializerBinder.addBinding().to(NativePropertySerializer.class);
		serializerBinder.addBinding().to(RecursiveSerializer.class);
	}
}
