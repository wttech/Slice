/*-
 * #%L
 * Slice - Mapper API
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

package com.cognifide.slice.mapper.annotation;

import com.cognifide.slice.mapper.api.Mapper;

/**
 * Defines what fields of classes annotated by {@link SliceResource} are mapped by {@link Mapper}
 * 
 */
public enum MappingStrategy {
	/**
	 * Only fields annotated by {@link JcrProperty} are mapped by {@link Mapper}. {@link IgnoreProperty} has
	 * no effect here.
	 */
	ANNOTATED,
	/**
	 * All fields except for the fields annotated by {@link IgnoreProperty} are mapped by {@link Mapper}
	 */
	ALL;
}
