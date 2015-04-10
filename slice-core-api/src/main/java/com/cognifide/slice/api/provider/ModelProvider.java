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

package com.cognifide.slice.api.provider;

import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;

import aQute.bnd.annotation.ProviderType;

import com.google.inject.Key;

/**
 * This class creates object or list of objects of given injectable type using Guice injector.
 * 
 * @author Witold Szczerba
 * @author Rafał Malinowski
 */
@ProviderType
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
	 * @return model object from given CRX repository path
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
	 * @return model object from given resource
	 */
	<T> T get(final Class<T> type, final Resource resource);

	/**
	 * Creates new model object of type T from given resource. During this method call state of
	 * {@link ExecutionContextStack} is modified - resource attribute is added on top of execution stack. It
	 * allows for recursive call of ModelProvider methods from model object that is being created.
	 * 
	 * This method is thread-safe.
	 * 
	 * @param T type of model object to create
	 * @param key a Guice {@link Key} which defines binding to a required object
	 * @param resource Sling resource to create object from
	 * @return model object from given resource
	 * 
	 * @see Key
	 */
	<T> T get(final Key<T> key, final Resource resource);

	/**
	 * Creates new model object of type T from given CRX repository path. During this method call state of
	 * {@link ExecutionContextStack} is modified - path attribute is added on top of execution stack. It
	 * allows for recursive call of ModelProvider methods from model object that is being created.
	 * 
	 * It is possible to use absolute and relative (with "./" prefix) paths in recursive calls. All gets are
	 * performed with Context that was used to create this ModelProvider.
	 * 
	 * This method is thread-safe.
	 * 
	 * @param T type of model object to create
	 * @param key a Guice {@link Key} which defines binding to a required object
	 * @param path CRX repository path to create object from
	 * @return model object from given CRX repository path
	 * 
	 * @see Key
	 */
	<T> T get(final Key<T> key, final String path);

	/**
	 * Creates new model object of type specified by given class name. During this method call state of
	 * {@link ExecutionContextStack} is modified - path attribute is added on top of execution stack. It
	 * allows for recursive call of ModelProvider methods from model object that is being created.
	 * 
	 * @param className canonical name of a class to be resolved, e.g. com.cognifide.slice.api.ModelProvider
	 * @param path CRX repository path to create object from
	 * @return model object of specified className from given CRX repository path
	 * @throws ClassNotFoundException if specified className cannot be resolved by Injector,
	 * {@link ClassNotFoundException} is thrown
	 */
	Object get(final String className, final String path) throws ClassNotFoundException;

	/**
	 * Creates new model object of type specified by given class name. During this method call state of
	 * {@link ExecutionContextStack} is modified - path attribute is added on top of execution stack. It
	 * allows for recursive call of ModelProvider methods from model object that is being created.
	 * 
	 * @param className canonical name of a class to be resolved, e.g. com.cognifide.slice.api.ModelProvider
	 * @param resource Sling resource to create object from
	 * @return model object from given resource
	 * @throws ClassNotFoundException if specified className cannot be resolved by Injector,
	 */
	Object get(final String className, final Resource resource) throws ClassNotFoundException;

	/**
	 * Creates new model object of type specified by the slice:model property defined in the component
	 * definition resource. During this method call state of {@link ExecutionContextStack} is modified - path
	 * attribute is added on top of execution stack. It allows for recursive call of ModelProvider methods
	 * from model object that is being created.
	 * 
	 * @param resource Sling resource to create object from
	 * @return model object from given resource or null if there is no slice:model property
	 * @throws ClassNotFoundException if specified className cannot be resolved by Injector,
	 */
	Object get(final Resource resource) throws ClassNotFoundException;

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
	 * @return list of model objects from given CRX repository paths
	 */
	<T> List<T> getList(final Class<T> type, final Iterator<String> paths);

	/**
	 * This is convenience method that works exactly like {@link #getList(Class, Iterator)}
	 * 
	 * @param T type of model objects to create
	 * @param type class of model objects to create
	 * @param paths array of CRX repository paths to create objects from
	 * @return list of model objects from given CRX repository paths
	 */
	<T> List<T> getList(final Class<T> type, final String[] paths);

	/**
	 * This is a convenience method working exactly like {@link ModelProvider#getChildModels(Class, Resource)}
	 * 
	 * @param <T> type of model objects to create
	 * @param type class of model objects to create
	 * @param parentPath Sling resource path to obtain children (could be relative)
	 * @return list of model objects from given CRX repository paths or empty list if parentPath is invalid
	 */
	<T> List<T> getChildModels(final Class<T> type, final String parentPath);

	/**
	 * This method lists children of the given resource via {@code parentResource.listChildren()}, invokes
	 * {@link ModelProvider#get(Class, Resource)} for each of them and returns a {@code List} containing
	 * mapped models. If the {@code parentResource} is null, an empty list will be returned.
	 * 
	 * @param <T> type of model objects to create
	 * @param type class of model objects to create
	 * @param parentResource Sling resource to obtain children
	 * @return list of model objects from given CRX repository paths or empty list if parentResource is null
	 */
	<T> List<T> getChildModels(final Class<T> type, final Resource parentResource);

	/**
	 * Creates list of models of type T from specified resources. Each resource from specified iterator is
	 * mapped to a class T
	 * 
	 * 
	 * @param <T> type of model objects to create
	 * @param type class of model objects to create
	 * @param resources iterator that returns CRX repository resources to create objects from
	 * @return list of model objects mapped from given resources. If the resource iterator has no element or
	 * is <code>null</code>, then an empty list is returned. It never returns <code>null</code>.
	 */
	<T> List<T> getListFromResources(final Class<T> type, final Iterator<Resource> resources);
}
