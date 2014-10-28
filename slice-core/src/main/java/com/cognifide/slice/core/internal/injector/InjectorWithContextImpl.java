package com.cognifide.slice.core.internal.injector;

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

import com.cognifide.slice.api.context.ContextProvider;
import com.cognifide.slice.api.context.ContextScope;
import com.cognifide.slice.api.injector.InjectorWithContext;
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * @author Rafał Malinowski
 * @class InjectorWithContext
 * 
 * Decoration for Guice Injector class with simple access to getting and setting ContextProvider. For use in
 * Servlets or Services use following idiom:
 * 
 * <pre>
 * {@code
 * final InjectorWithContext injector = InjectorsRepository.getInjector(APP_NAME);
 * final ContextProvider previousContextProvider = injector.getContextProvider();
 * final SimpleContextProvider simpleContextProvider = new SimpleContextProvider();
 * simpleContextProvider.setContext(ContextFactory.getServletRequestContext(request, response));
 * 
 * injector.setContextProvider(simpleContextProvider);
 * try {
 *   ...
 * } finally {
 *   injector.setContextProvider(previousContextProvider);
 * }
 * }
 * </pre>
 * 
 * to ensure that injector is left in a state that is was before.
 * 
 * This decoration has two delegate getInstance() methods added for convenience.
 */
public class InjectorWithContextImpl implements InjectorWithContext {

	private final Injector injector;

	private final ContextScope contextScope;

	/**
	 * Create decoration for Injector.
	 * 
	 * @param injector Injector to decorate
	 */
	public InjectorWithContextImpl(final Injector injector) {
		this.injector = injector;
		this.contextScope = injector.getInstance(ContextScope.class);
	}

	/**
	 * Return decorated Injector.
	 * 
	 * @return decorated Injector.
	 */
	public Injector getInjector() {
		return injector;
	}

	/**
	 * Set new ContextProvider for decorated Injector
	 * 
	 * @param contextProvider new ContextProvider
	 */
	@Override
	public void setContextProvider(final ContextProvider contextProvider) {
		contextScope.setContextProvider(contextProvider);
	}

	/**
	 * Get current ContextProvider for decorated Injector
	 * 
	 * @return current ContextProvider
	 */
	@Override
	public ContextProvider getContextProvider() {
		return contextScope.getContextProvider();
	}

	@Override
	public <T> T getInstance(final Class<T> clazz) {
		return injector.getInstance(clazz);
	}

	@Override
	public Object getInstance(final Key<?> key) {
		return injector.getInstance(key);
	}

	/** {@inheritDoc} */
	@Override
	public Object getInstance(String className) throws ClassNotFoundException {
		Class<?> clazz = Class.forName(className);
		return getInstance(clazz);
	}
}
