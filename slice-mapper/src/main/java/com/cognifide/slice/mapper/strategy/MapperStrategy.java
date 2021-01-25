/*-
 * #%L
 * Slice - Mapper
 * %%
 * Copyright (C) 2012 Wunderman Thompson Technology
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

package com.cognifide.slice.mapper.strategy;

import java.lang.reflect.Field;

import com.cognifide.slice.mapper.api.Mapper;

/**
 * Defines if given fields should be mapped by {@link Mapper}.
 */
public interface MapperStrategy {

	/**
	 * @return <code>true</code> if the specified field should be mapped by {@link Mapper}, otherwise returns
	 * <code>false</code>
	 * 
	 * @param field field to be checked
	 */
	boolean shouldFieldBeMapped(Field field);
}
