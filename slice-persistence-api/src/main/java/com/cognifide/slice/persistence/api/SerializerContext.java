/*-
 * #%L
 * Slice - Persistence API
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
package com.cognifide.slice.persistence.api;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

import org.apache.jackrabbit.JcrConstants;

import aQute.bnd.annotation.ProviderType;

/**
 * Serialization context, provides a state to the serialization process.
 *
 * @since 4.3
 */
@ProviderType
public class SerializerContext {

	private final Map<Object, Object> alreadySerialized;

	private final SerializerFacade facade;

	public SerializerContext(SerializerFacade facade) {
		this.alreadySerialized = new IdentityHashMap<Object, Object>();
		this.facade = facade;
	}

	public boolean alreadySerialized(Object object) {
		return Boolean.TRUE.equals(alreadySerialized.put(object, Boolean.TRUE));
	}

	public Map<String, Object> getInitialProperties() {
		return Collections.singletonMap(JcrConstants.JCR_PRIMARYTYPE, (Object) JcrConstants.NT_UNSTRUCTURED);
	}

	/**
	 * Returns the {@link SerializerFacade} object.
	 **/
	public SerializerFacade getFacade() {
		return facade;
	}
}
