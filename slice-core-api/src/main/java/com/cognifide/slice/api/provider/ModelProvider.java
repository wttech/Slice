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

import com.cognifide.slice.api.context.Context;
import com.cognifide.slice.mapper.annotation.SliceResource;
import com.cognifide.slice.mapper.api.Mapper;
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * A base interface in Slice. It allows to fetch an injectable object(s) mapped from specified resource or
 * path. It resembles Guice com.google.inject.Injector in being used for fetching objects of different classes
 * but it is closely tied to Slice because it also support mappings of requested objects with data obtained
 * from specified resources.
 * 
 * ModelProvider can be also injected directly to your models, so that you can read a model from an arbitrary
 * resource or path, e.g.:
 * 
 * <pre>
 *  {@literal @}SliceResource
 *  public class OrderModel {
 * 
 *    private final static String CONFIGURATION_PATH = "/content/app/configuration/jcr:content/currency";
 *    private final ModelProvider modelProvider;
 * 
 *    {@literal @}JcrProperty
 *    private int value;
 * 
 *    {@literal @}Inject
 *    public OrderModel(ModelProvider modelProvider) {
 *      this.modelProvider = modelProvider;
 *    }
 * 
 *    public int getValue() {
 *      Currency currency = modelProvider.get(Currency.class, CONFIGURATION_PATH);
 *      return formatToCurrency(value, currency);
 *    }
 *  ...
 *  }
 * </pre>
 * 
 * All methods of ModelProvider are thread safe
 * 
 */

@ProviderType
public interface ModelProvider {

	/**
	 * Instantiates an object of specified type using Guice injector. Additionally, Slice remembers the
	 * resource of specified path internally, so it can be either injected or used for mapping of objects
	 * annotated with {@link SliceResource}. Mapping is triggered automatically by Slice (with use of
	 * {@link Mapper}), so if the specified class is annotated with {@link SliceResource} it will be mapped
	 * from specified resource.<br>
	 * <br>
	 * Please note that objects which are injected to instantiated one, can also leverage specified resource,
	 * so they can also be mapped automatically. Let's consider an example.
	 * 
	 * <pre>
	 *  {@literal @}SliceResource
	 *  public class Order {
	 *    {@literal @}JcrProperty
	 *    String orderProperty;
	 *    
	 *    {@literal @}Inject
	 *    public Order(Currency currency) {
	 *    ..
	 *    }
	 *  ..
	 *  }
	 *  
	 *  {@literal @}SliceResource
	 *  public class Currency {
	 *    {@literal @}JcrProperty
	 *    String currencyProperty;
	 *   ..
	 *  }
	 *  ..
	 *  modelProvider.get(Order.class, "/content/app/orders/order1");
	 * </pre>
	 * 
	 * In above execution, both Order and Currency objects will be mapped from resource defined by
	 * /content/app/orders/order1. It means that the above code expects the resource to contain two
	 * properties: orderProperty, currencyProperty. <br>
	 * <br>
	 * It is possible to use absolute and relative (with "./" prefix) paths in recursive calls. All get
	 * methods are invoked with {@link Context} that was used to create this ModelProvider. Example:
	 * 
	 * <pre>
	 *  modelProvider.get(Order.class, "/content/app/orders/order1");
	 *   
	 *  public class Order {
	 *    {@literal @}Inject
	 *    public Order(ModelProvider modelProvider) {
	 *       modelProvider.get(Currency.class, "./currency");
	 *    }
	 *  }
	 * </pre>
	 * 
	 * In above case, Currency object will be mapped from a resource defined by
	 * /content/app/orders/order1/currency.
	 * 
	 * @param <T> type of object to be created
	 * @param type class of object to be created
	 * @param path Path to a resource which will be used to map @SliceResource objects
	 * @return instantiated object of specified type. If the type is annotated by {@link SliceResource} it
	 * will be mapped from resource from under specified path
	 */
	<T> T get(final Class<T> type, final String path);

	/**
	 * Instantiates an object of specified type using Guice injector and map it from specified resource. It
	 * works exactly as {@link #get(Class, String) get(type, path)}.
	 * 
	 * @param <T> type of object to be created
	 * @param type class of object to be created
	 * @param resource resource which will be used to map @SliceResource objects
	 * @return instantiated object of specified type. If the type is annotated by {@link SliceResource} it
	 * will be mapped from resource from under specified path
	 */
	<T> T get(final Class<T> type, final Resource resource);

	/**
	 * Instantiates an object of specified type using Guice injector and specified {@link Key}. It maps the
	 * object from specified resource, exactly as described in {@link #get(Class, String) get(type, path)}.
	 * 
	 * @param <T> type of model object to be created
	 * @param key a Guice {@link Key} which defines binding to a required object
	 * @param resource resource which will be used to map @SliceResource objects
	 * @return instantiated object of specified type. If the type is annotated by {@link SliceResource} it
	 * will be mapped from resource from under specified path
	 * 
	 * @see Injector#getInstance(Key)
	 */
	<T> T get(final Key<T> key, final Resource resource);

	/**
	 * Instantiates an object of specified type using Guice injector and specified {@link Key}. It maps the
	 * object from specified resource, exactly as described in {@link #get(Class, String) get(type, path)}.
	 * 
	 * @param <T> type of model object to be created
	 * @param key a Guice {@link Key} which defines binding to a required object
	 * @param path path to a resource which will be used to map @SliceResource objects
	 * @return instantiated object of specified type. If the type is annotated by {@link SliceResource} it
	 * will be mapped from resource from under specified path
	 * 
	 * @see Injector#getInstance(Key)
	 */
	<T> T get(final Key<T> key, final String path);

	/**
	 * Instantiates an object of specified type using Guice injector and map it from specified resource. It
	 * works exactly as {@link #get(Class, String) get(type, path)}.
	 * 
	 * @param className canonical name of a class to be resolved, e.g. com.cognifide.slice.api.ModelProvider
	 * @param path path to a resource which will be used to map @SliceResource objects
	 * @return instantiated object of specified type. If the type is annotated by {@link SliceResource} it
	 * will be mapped from resource from under specified path
	 * @throws ClassNotFoundException if specified className cannot be loaded
	 */
	Object get(final String className, final String path) throws ClassNotFoundException;

	/**
	 * Instantiates an object of specified type using Guice injector and map it from specified resource. It
	 * works exactly as {@link #get(Class, String) get(type, path)}.
	 * 
	 * @param className canonical name of a class to be resolved, e.g. com.cognifide.example.MyClass
	 * @param resource resource which will be used to map @SliceResource objects
	 * @return instantiated object of specified type. If the type is annotated by {@link SliceResource} it
	 * will be mapped from resource from under specified path
	 * @throws ClassNotFoundException if specified className cannot be loaded
	 */
	Object get(final String className, final Resource resource) throws ClassNotFoundException;

	/**
	 * Creates new model object of type specified by the slice:model property defined in the component
	 * definition resource (basically resource indicated by sling:resourceType). It works exactly as
	 * {@link #get(String, Resource)} where String property is read from component definition resource.
	 * 
	 * @param resource resource which will be used to map @SliceResource objects
	 * @return model object from given resource or null if there is no slice:model property in component
	 * definition resource
	 * @throws ClassNotFoundException if class found under slice:model cannot be loaded
	 */
	Object get(final Resource resource) throws ClassNotFoundException;

	/**
	 * This is convenience method that works exactly like {@link #getListFromResources(Class, Iterator)}
	 * 
	 * @param <T> type of model objects to be created
	 * @param type class of model objects to be created
	 * @param paths iterator that returns paths to resources from which objects will be mapped
	 * @return list of model objects mapped from given resources. If the resource iterator has no element or
	 * is <code>null</code>, then an empty list is returned. It never returns <code>null</code>.
	 */
	<T> List<T> getList(final Class<T> type, final Iterator<String> paths);

	/**
	 * This is convenience method that works exactly like {@link #getList(Class, Iterator)}
	 * 
	 * @param <T> type of model objects to create
	 * @param type class of model objects to create
	 * @param paths array of Sling repository paths to create objects from
	 * @return list of model objects from given Sling repository paths
	 */
	<T> List<T> getList(final Class<T> type, final String[] paths);

	/**
	 * This is a convenience method working exactly like {@link ModelProvider#getChildModels(Class, Resource)}
	 * 
	 * @param <T> type of model objects to create
	 * @param type class of model objects to create
	 * @param parentPath Sling resource path to obtain children (could be relative)
	 * @return list of model objects from given Sling repository paths or empty list if parentPath is invalid
	 */
	<T> List<T> getChildModels(final Class<T> type, final String parentPath);

	/**
	 * This method lists children of the given resource via {@code parentResource.listChildren()}, invokes
	 * {@link ModelProvider#get(Class, Resource)} for each of them and returns a {@code List} containing
	 * mapped models. If the {@code parentResource} is null or doesn't exist, an empty list will be returned.
	 * 
	 * @param <T> type of model objects to be created
	 * @param type class of model objects to be created
	 * @param parentResource Sling resource to obtain children resources
	 * @return list of model objects from given Sling repository paths or empty list if parentResource is null
	 * or doesn't exist
	 */
	<T> List<T> getChildModels(final Class<T> type, final Resource parentResource);

	/**
	 * Creates list of models of type T from specified resources. Each resource from specified iterator is
	 * mapped to a class T
	 * 
	 * 
	 * @param <T> type of model objects to create
	 * @param type class of model objects to create
	 * @param resources iterator that returns Sling repository resources to create objects from
	 * @return list of model objects mapped from given resources. If the resource iterator has no element or
	 * is <code>null</code>, then an empty list is returned. It never returns <code>null</code>.
	 */
	<T> List<T> getListFromResources(final Class<T> type, final Iterator<Resource> resources);
}
