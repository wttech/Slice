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
package com.cognifide.slice.mapper.impl.processor.adapter;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public enum AssignableChildrenFieldType {
	COLLECTION(Collection.class, new ListAdapter()),
	LIST(List.class, new ListAdapter()),
	SET(Set.class, new ListToSetAdapter()),
	SORTED_SET(SortedSet.class, new ListToSortedSetAdapter());

	private final Class<?> clazz;

	private final MappedListAdapter adapter;

	AssignableChildrenFieldType(Class<?> clazz, MappedListAdapter adapter) {
		this.clazz = clazz;
		this.adapter = adapter;
	}

	public static AssignableChildrenFieldType byClass(Class<?> clazz) {
		for (AssignableChildrenFieldType type : values()) {
			if (type.clazz.equals(clazz)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Cannot find AssignableFieldType by class: " + clazz);
	}

	public static boolean contains(Class<?> clazz) {
		for (AssignableChildrenFieldType type : values()) {
			if (type.clazz.equals(clazz)) {
				return true;
			}
		}
		return false;
	}

	public MappedListAdapter getAdapter() {
		return adapter;
	}
}
