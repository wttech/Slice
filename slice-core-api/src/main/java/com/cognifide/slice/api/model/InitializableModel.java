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

package com.cognifide.slice.api.model;

/**
 * Allows a model to be informed after mapping from resource has been finished and all its fields have been
 * mapped to appropriate values of corresponding resource.<br>
 * <br>
 * Please note the difference between creating an object and initializing it. When the constructor of the
 * model has finished, the object is created but it is still not mapped by the {@link Mapper}. When the Mapper
 * finishes mapping process, the {@link #afterCreated()} method is called to do an arbitrary logic.
 * 
 * @author maciej.majchrzak
 * 
 */
public interface InitializableModel {

	/**
	 * It is called after the SliceResource model has been mapped to a corresponding resource. It can do an
	 * arbitrary logic.
	 */
	void afterCreated();

}
