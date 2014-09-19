/*-
 * #%L
 * Slice - Core
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2012 - 2014 Cognifide Limited
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

package com.cognifide.slice.core.internal.module;

import org.apache.sling.api.resource.Resource;
import org.ops4j.peaberry.Peaberry;
import org.osgi.framework.Bundle;

import com.cognifide.slice.api.context.ContextFactory;
import com.cognifide.slice.api.context.ContextScope;
import com.cognifide.slice.api.execution.ExecutionContextStack;
import com.cognifide.slice.api.injector.InjectorsRepository;
import com.cognifide.slice.api.provider.ChildrenProvider;
import com.cognifide.slice.api.provider.ModelProvider;
import com.cognifide.slice.api.qualifier.CurrentResourcePath;
import com.cognifide.slice.api.qualifier.InjectorName;
import com.cognifide.slice.api.qualifier.Nullable;
import com.cognifide.slice.api.qualifier.RequestedResource;
import com.cognifide.slice.api.qualifier.RequestedResourcePath;
import com.cognifide.slice.api.scope.ContextScoped;
import com.cognifide.slice.commons.module.ContextScopeModule;
import com.cognifide.slice.core.internal.context.SliceContextFactory;
import com.cognifide.slice.core.internal.execution.ExecutionContextStackImpl;
import com.cognifide.slice.core.internal.provider.SliceChildrenProvider;
import com.cognifide.slice.core.internal.provider.SliceModelProvider;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public final class SliceModule extends ContextScopeModule {

	private static final String DEFAULT_ROOT_PATH = "/content/";

	private final Bundle bundle;

	public SliceModule(ContextScope contextScope, Bundle bundle) {
		super(contextScope);
		this.bundle = bundle;
	}

	@Override
	protected void configure() {
		bind(InjectorsRepository.class).toProvider(
				Peaberry.service(InjectorsRepository.class).single().direct());

		bindScope(ContextScoped.class, getContextScope());
		bind(ContextScope.class).toInstance(getContextScope());

		bind(ModelProvider.class).to(SliceModelProvider.class);
		bind(ChildrenProvider.class).to(SliceChildrenProvider.class);
		bind(ContextFactory.class).to(SliceContextFactory.class);

		bindToContextScope(Key.get(Resource.class, RequestedResource.class));
	}

	@Provides
	@ContextScoped
	public ExecutionContextStack getCurrentExecutionContext() {
		return new ExecutionContextStackImpl(DEFAULT_ROOT_PATH);
	}

	@Provides
	@CurrentResourcePath
	public String getCurrentResourcePath(final ExecutionContextStack currentExecutionContext) {
		return currentExecutionContext.peek().getPath();
	}

	@Provides
	@RequestedResourcePath
	public String getRequestedResourcePath(@Nullable @RequestedResource final Resource requestedResource) {
		return requestedResource == null ? null : requestedResource.getPath();
	}

	@Provides
	@InjectorName
	public String getInjectorName(InjectorsRepository repository, Injector injector) {
		return repository.getInjectorName(injector);
	}

	@Provides
	@Singleton
	public Bundle getBundle() {
		return bundle;
	}
}
