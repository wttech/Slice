package com.cognifide.slice.api.injector;

/*-
 * #%L
 * Slice - Core API
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
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * @author Rafał Malinowski
 * @class InjectorWithContext
 * 
 * Decoration for Guice Injector class with simple access to modyfing and restoring ContextProvider. For use
 * in Servlets or Services use following code:
 * 
 * <pre>
 * {@code
 * @Reference
 * private InjectorsRepository injectorsRepository;
 * 
 * final InjectorWithContext injector = injectorsRepository.getInjector(APP_NAME);
 * final ContextProvider previousContextProvider = injector.getContextProvider();
 * final ContextProviderFactory contextProviderFactory = injector.getInstance(ContextProviderFactory.class);
 * final SimpleContextProvider simpleContextProvider = contextProviderFactory.getSimpleContextProvider();
 * final ContextFactory contextFactory = injector.getInstance(ContextFactory.class);
 * simpleContextProvider.setContext(contextFactory.getServletRequestContext(request, response));
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
public interface InjectorWithContext {

	/**
	 * Return Guice Injector behind this InjectorWithContext.
	 */
	Injector getInjector();

	/**
	 * Set ContextProvider for Guice Injector.
	 *
	 * @param contextProvider contextProvider to be set as current on Guice Injector.
	 */
	void setContextProvider(final ContextProvider contextProvider);

	/**
	 * Return current ContextProvider for Guice Injector.
	 * 
	 * @return current ContextProvider
	 */
	ContextProvider getContextProvider();

	/**
	 * Return new instance of given class using Guice Injector.
	 */
	<T> T getInstance(final Class<T> clazz);

	/**
	 * Return new instance by given key using Guice Injector.
	 */
	Object getInstance(final Key<?> key);

	/**
	 * Creates an object of the given class.
	 * 
	 * @param className fully qualified class name.
	 * @return new instance of an object, never null.
	 * @throws ClassNotFoundException if the class cannot be located.
	 */
	Object getInstance(String className) throws ClassNotFoundException;
}
