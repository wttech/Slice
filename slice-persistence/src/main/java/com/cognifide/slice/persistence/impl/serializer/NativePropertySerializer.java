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
package com.cognifide.slice.persistence.impl.serializer;

import java.io.InputStream;
import java.util.Calendar;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;

import com.cognifide.slice.persistence.api.ObjectSerializer;
import com.cognifide.slice.persistence.api.Serializer;
import com.cognifide.slice.persistence.api.SerializerContext;

@Component(immediate = true)
@Service(Serializer.class)
public class NativePropertySerializer implements ObjectSerializer {

	private static final Class<?>[] SUPPORTED_CLASSES = new Class<?>[] { int.class, Integer.class,
			long.class, Long.class, Calendar.class, double.class, Double.class, boolean.class, Boolean.class,
			String.class, InputStream.class };

	@Override
	public int getPriority() {
		return 100;
	}

	@Override
	public boolean accepts(Class<?> clazz) {
		return isNativeJcrClass(clazz);
	}

	static boolean isNativeJcrClass(Class<?> clazz) {
		for (Class<?> supportedClass : SUPPORTED_CLASSES) {
			if (supportedClass.isAssignableFrom(clazz)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void serialize(String propertyName, Object object, Resource parent, SerializerContext ctx)
			throws PersistenceException {
		final ModifiableValueMap map = parent.adaptTo(ModifiableValueMap.class);

		if (object == null) {
			map.remove(propertyName);
		} else {
			map.put(propertyName, object);
		}
	}
}
