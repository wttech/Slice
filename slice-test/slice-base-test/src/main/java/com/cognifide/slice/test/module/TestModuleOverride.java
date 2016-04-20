/*-
 * #%L
 * Slice - Tests Base
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
package com.cognifide.slice.test.module;

import com.cognifide.slice.api.scope.ContextScoped;
import com.cognifide.slice.mapper.api.SliceReferencePathResolver;
import com.cognifide.slice.mapper.impl.SliceReferencePathResolverImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;

/**
 * @author Mariusz Kubiś Date: 15.04.15
 */
public class TestModuleOverride extends AbstractModule {

	@Override
	protected void configure() {

	}

	@Provides
	@ContextScoped
	public SliceReferencePathResolver getSliceReferencePathResolver(Injector injector) {
		SliceReferencePathResolver sliceReferencePathResolver = new SliceReferencePathResolverImpl(injector);
		sliceReferencePathResolver.addPlaceholder("a1_", "");
		return sliceReferencePathResolver;
	}
}
