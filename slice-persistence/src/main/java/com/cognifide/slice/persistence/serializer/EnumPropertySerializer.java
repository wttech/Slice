/*-
 * #%L
 * Slice - Persistence
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
package com.cognifide.slice.persistence.serializer;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;

import com.cognifide.slice.persistence.api.ObjectSerializer;
import com.cognifide.slice.persistence.api.SerializerContext;

public class EnumPropertySerializer implements ObjectSerializer {

	@Override
	public boolean accepts(Class<?> clazz) {
		return clazz.isEnum();
	}

	@Override
	public void serialize(String propertyName, Object object, Resource parent, SerializerContext ctx)
			throws PersistenceException {
		final ModifiableValueMap map = parent.adaptTo(ModifiableValueMap.class);
		final Enum<?> e = (Enum<?>) object;
		map.put(propertyName, e.name());
	}

	@Override
	public int getRank() {
		return 0;
	}
}
