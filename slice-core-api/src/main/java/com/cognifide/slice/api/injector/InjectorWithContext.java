/*-
 * #%L
 * Slice - Core API
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

package com.cognifide.slice.api.injector;

import java.io.Closeable;

import aQute.bnd.annotation.ProviderType;

import com.cognifide.slice.api.context.ContextProvider;
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * Decoration for Guice Injector class with simple access to modyfing and restoring ContextProvider. This
 * decoration has three delegate getInstance() methods added for convenience.
 */
@ProviderType
public interface InjectorWithContext extends Closeable {

	/**
	 * Return Guice Injector behind this InjectorWithContext.
	 */
	Injector getInjector();

	/**
	 * Push new ContextProvider on ContextProvider stack and set it as current on Guice Injector.
	 */
	void pushContextProvider(final ContextProvider contextProvider);

	/**
	 * Pop top ContextProvider from ContextProvider stack and set previous one as current on Guice Injector.
	 */
	ContextProvider popContextProvider();

	/**
	 * Easy to remember alias for the {@code #popContextProvider()} method.
	 */
	void close();

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
