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
 * A base interface in Slice. It allows to fetch an injectable object(s) mapped from specified
 * resource or path. It resembles Guice com.google.inject.Injector ecause it is used for fetching objects
 * of different classes but it is closely tied to Slice because if fills requested objects with data obtained
 * from Sling JCR.
 *
 * ModelProvider can be also injected directly to your models, so that you can read a model from an arbitrary
 * resource or path, e.g.:
 * <pre>
 *{@literal @}SliceResource
 * public class OrderModel {
 *
 * private final static String CONFIGURATION_PATH = "/content/app/configuration/jcr:content/currency";
 * private final ModelProvider modelProvider;
 *
 * {@literal @}JcrProperty
 * private int value;
 *
 * {@literal @}Inject
 * public OrderModel(ModelProvider modelProvider) {
 * 	this.modelProvider = modelProvider;
 * }
 *
 * public int getValue() {
 * 	Currency currency = modelProvider.get(Currency.class, CONFIGURATION_PATH);
 * 	return formatToCurrency(value, currency);
 * 	}
 * 	...
 * 	}
 * </pre>
 *
 * All methods of ModelProvider are thread safe
 *
 * @author Witold Szczerba
 * @author Rafał Malinowski
 */

@ProviderType
public interface ModelProvider {

	/**
	 * Creates new model object of type T and fills all its matching properties with data found under
	 * given Sling repository path.
	 *
	 * Lets consider an example. There is model:
	 *
	 * <pre>
	 *{@literal @}SliceResource
	 * public class OrderModel {
	 *
	 *{@literal @}JcrProperty("orderValue")
	 * private int value;
	 *
	 *{@literal @}JcrProperty
	 * private Date deliveryDate;
	 *
	 *{@literal @}JcrProperty
	 * private String status;
	 *
	 * }
	 * </pre>
	 *
	 * There is also a node in Sling JCR under '/content/app/orders/order-id-12345' and it has the
	 * following properties:<br>
	 * type:Decimal, name:'orderValue', value:1000<br>
	 * type:String, name:'status', value:"incomplete"<br>
	 * type:String, name:'createdBy', value:"admin"<br>
	 *	<br>
	 * Somewhere in the code there is an invocation:<br>
	 * <code>
	 *     OrderModel model = modelProvider.get(OrderModel.class, "/content/app/orders/order-id-12345");
	 * </code>
	 * <br><br>
	 * In this case the result model will have:<br>
	 * value=1000,<br>
	 * status="incomplete"<br>
	 * deliveryDate=null<br>
	 * <br>
	 * It is possible to use absolute and relative (with "./" prefix) paths in recursive calls. All gets are
	 * performed with Context that was used to create this ModelProvider.
	 *
	 * <code>
	 *   modelProvider.get(ModelType.class, "/absolute/patch");
	 *   modelProvider.get(SubModelType.class, "./relative/path");
	 * </code>
	 *
	 * @param <T> type of model object to create
	 * @param type class of model object to create
	 * @param path Sling repository path to create object from
	 * @return model object from given Sling repository path
	 */
	<T> T get(final Class<T> type, final String path);

	/**
	 * Creates new model object of type T and fills all its matching properties with data obtained from
	 * given resource. The concept is the same as in {@link #get(Class, String) get}
	 * 
	 * @parem T type of model object to create
	 * @param type class of model object to create
	 * @param resource Sling resource to create object from
	 * @return model object from given resource
	 */
	<T> T get(final Class<T> type, final Resource resource);

	/**
	 * Creates new model object of type T and fills all its matching properties with data obtained from
	 * given resource. The concept is the same as in {@link #get(Class, String) get}
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
	 * Creates new model object of type T and fills all its matching properties with data found under
	 * given Sling repository path. The concept is the same as in {@link #get(Class, String) get}
	 * 
	 * @param T type of model object to create
	 * @param key a Guice {@link Key} which defines binding to a required object
	 * @param path Sling repository path to create object from
	 * @return model object from given Sling repository path
	 * 
	 * @see Key
	 */
	<T> T get(final Key<T> key, final String path);

	/**
	 * Creates new model object of type T and fills all its matching properties with data found under
	 * given Sling repository path. The concept is the same as in {@link #get(Class, String) get}
	 *
	 * @param className canonical name of a class to be resolved, e.g. com.cognifide.slice.api.ModelProvider
	 * @param path Sling repository path to create object from
	 * @return model object of specified className from given Sling repository path
	 * @throws ClassNotFoundException if specified className cannot be resolved by Injector,
	 * {@link ClassNotFoundException} is thrown
	 */
	Object get(final String className, final String path) throws ClassNotFoundException;

	/**
	 * Creates new model object of type T and fills all its matching properties with data found under
	 * given Sling repository path. The concept is the same as in {@link #get(Class, String) get}
	 *
	 * @param className canonical name of a class to be resolved, e.g. com.cognifide.slice.api.ModelProvider
	 * @param resource Sling resource to create object from
	 * @return model object from given resource
	 * @throws ClassNotFoundException if specified className cannot be resolved by Injector,
	 */
	Object get(final String className, final Resource resource) throws ClassNotFoundException;

	/**
	 * Creates new model object of type T and fills all its matching properties with data obtained from
	 * given resource. The concept is the same as in {@link #get(Class, String) get}
	 * 
	 * It is possible to use absolute and relative (with "./" prefix) paths in recursive calls.
	 * 
	 * @param T type of model objects to create
	 * @param type class of model objects to create
	 * @param paths iterator that returns Sling repository paths to create objects from
	 * @return list of model objects from given Sling repository paths
	 */
	<T> List<T> getList(final Class<T> type, final Iterator<String> paths);

	/**
	 * This is convenience method that works exactly like {@link #getList(Class, Iterator)}
	 * 
	 * @param T type of model objects to create
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
	 * mapped models. If the {@code parentResource} is null, an empty list will be returned.
	 * 
	 * @param <T> type of model objects to create
	 * @param type class of model objects to create
	 * @param parentResource Sling resource to obtain children
	 * @return list of model objects from given Sling repository paths or empty list if parentResource is null
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
