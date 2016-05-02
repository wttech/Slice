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

import java.util.Stack;

import com.cognifide.slice.api.context.ContextProvider;
import com.cognifide.slice.api.context.ContextScope;
import com.cognifide.slice.api.injector.InjectorWithContext;
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * @author Rafał Malinowski
 * 
 * Decoration for Guice Injector class with simple access to modyfing and restoring ContextProvider. For use
 * in Servlets or Services use following idiom:
 * 
 * <pre>
 * {@code
 * final InjectorWithContext injector = InjectorsRepository.getInjector(APP_NAME);
 * final SimpleContextProvider simpleContextProvider = new SimpleContextProvider();
 * simpleContextProvider.setContext(ContextFactory.getServletRequestContext(request, response));
 * 
 * injector.pushContextProvider(simpleContextProvider);
 * try {
 *   ...
 * } finally {
 *   injector.popContextProvider();
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

	private final Stack<ContextProvider> contextProviders;

	/**
	 * Create decoration for Injector.
	 * 
	 * @param injector Injector to decorate
	 */
	public InjectorWithContextImpl(final Injector injector) {
		this.injector = injector;
		this.contextScope = injector.getInstance(ContextScope.class);
		this.contextProviders = new Stack<ContextProvider>();
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
	 * Set new ContextProvider for decorated Injector. Push previous one on internal stack.
	 * 
	 * @param contextProvider new ContextProvider
	 */
	@Override
	public void pushContextProvider(final ContextProvider contextProvider) {
		contextProviders.push(contextScope.getContextProvider());
		contextScope.setContextProvider(contextProvider);
	}

	/**
	 * Restore previous ContextProvider for decorated Injector. Returns current one.
	 * 
	 * @return current ContextProvider
	 */
	@Override
	public ContextProvider popContextProvider() {
		final ContextProvider result = contextScope.getContextProvider();
		contextScope.setContextProvider(contextProviders.pop());
		return result;
	}

	/**
	 * Alias for the {@link #popContextProvider()} method.
	 * 
	 */
	public void close() {
		popContextProvider();
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
