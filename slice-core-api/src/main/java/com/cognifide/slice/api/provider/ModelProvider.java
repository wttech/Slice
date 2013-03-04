package com.cognifide.slice.api.provider;

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

import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;

/**
 * This class creates object or list of objects of given injectable type using Guice injector.
 * 
 * @author Witold Szczerba
 * @author Rafał Malinowski
 */
public interface ModelProvider {

	/**
	 * Creates new model object of type T from given CRX repository path. During this method call state of
	 * {@link ExecutionContextStack} is modified - path attribute is added on top of execution stack. It
	 * allows for recursive call of ModelProvider methods from model object that is being created.
	 * 
	 * It is possible to use absolute and relative (with "./" prefix) paths in recursive calls. All gets are
	 * performed with Context that was used to create this ModelProvider.
	 * 
	 * <code>
	 *   modelProvider.get(ModelType.class, "/absolute/patch");
	 *   modelProvider.get(SubModelType.class, "./relative/path");
	 * </code>
	 * 
	 * This method is thread-safe.
	 * 
	 * @parem T type of model object to create
	 * @param type class of model object to create
	 * @param path CRX repository path to create object from
	 * @return model object from given CRX repository path. It may return <code>null</code> depending on
	 * provider implementation for specified class.
	 * @see com.google.inject.Injector#getInstance(com.google.inject.Key) Injector.getInstance.
	 */
	<T> T get(final Class<T> type, final String path);

	/**
	 * Creates new model object of type T from given resource. During this method call state of
	 * {@link ExecutionContextStack} is modified - resource attribute is added on top of execution stack. It
	 * allows for recursive call of ModelProvider methods from model object that is being created.
	 * 
	 * This method is thread-safe.
	 * 
	 * @parem T type of model object to create
	 * @param type class of model object to create
	 * @param resource Sling resource to create object from
	 * @return model object from given resource. It may return <code>null</code> depending on provider
	 * implementation for specified class.
	 * @see com.google.inject.Injector#getInstance(com.google.inject.Key) Injector.getInstance.
	 */
	<T> T get(final Class<T> type, final Resource resource);

	/**
	 * Creates new model object of type specified by given class name. During this method call state of
	 * {@link ExecutionContextStack} is modified - path attribute is added on top of execution stack. It
	 * allows for recursive call of ModelProvider methods from model object that is being created.
	 * 
	 * @param className canonical name of a class to be resolved, e.g. com.cognifide.slice.api.ModelProvider
	 * @param path CRX repository path to create object from
	 * @return model object of specified className from given CRX repository path. It may return
	 * <code>null</code> depending on provider implementation for specified class.
	 * @see com.google.inject.Injector#getInstance(com.google.inject.Key) Injector.getInstance.
	 * @throws ClassNotFoundException if specified className cannot be resolved by Injector.
	 * {@link ClassNotFoundException} is thrown
	 */
	Object get(final String className, final String path) throws ClassNotFoundException;

	/**
	 * Creates list of model objects of type T from given CRX repository paths. During this method call state
	 * of {@link ExecutionContextStack} is modified for each path from iterator - path is added on top of path
	 * stack. It allows for recursive call of ModelProvider methods from model objects that are being created.
	 * 
	 * It is possible to use absolute and relative (with "./" prefix) paths in recursive calls.
	 * 
	 * @param T type of model objects to create
	 * @param type class of model objects to create
	 * @param paths iterator that returns CRX repository paths to create objects from
	 * @return list of model objects from given CRX repository paths. Returns empty list if <i>paths</i>
	 * argument is <code>null</code>.
	 */
	<T> List<T> getList(final Class<T> type, final Iterator<String> paths);

	/**
	 * This is convenience method that works exactly like {@link getList}.
	 * 
	 * @param T type of model objects to create
	 * @param type class of model objects to create
	 * @param paths array of CRX repository paths to create objects from
	 * @return list of model objects from given CRX repository paths. Returns empty list if <i>paths</i>
	 * argument is <code>null</code>.
	 */
	<T> List<T> getList(final Class<T> type, final String[] paths);
}