/*-
 * #%L
 * Slice - Core
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

package com.cognifide.slice.core.internal.context;

import java.util.concurrent.ConcurrentHashMap;

import com.cognifide.slice.api.scope.ContextScoped;
import com.google.inject.Key;

/**
 * Concurrent HashMap with wrapped key CacheableContextKey, storing slice models as values. The values are
 * stored per request.
 * 
 * @author kamil.ciecierski
 */
@ContextScoped
public class CacheableContext extends ConcurrentHashMap<CacheableContextKey, Object> {

	@SuppressWarnings("unchecked")
	public <T> T putIfAbsent(String path, Key<T> type, T value) {
		CacheableContextKey contextKey = new CacheableContextKey(path, type);
		return (T) putIfAbsent(contextKey, value);
	}

}
