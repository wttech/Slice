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

import org.ops4j.peaberry.Peaberry;

import com.cognifide.slice.persistence.api.ModelPersister;
import com.google.inject.AbstractModule;

public class PersistenceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ModelPersister.class).toProvider(Peaberry.service(ModelPersister.class).single().direct());
	}
}
