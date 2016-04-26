/*-
 * #%L
 * Slice - Core
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
package com.cognifide.slice.core.internal.module;

import java.lang.reflect.Method;

class MethodWrapper {

	public static final MethodWrapper EMPTY = new MethodWrapper(null);

	private final Method method;

	MethodWrapper(Method method) {
		this.method = method;
	}

	public Method get() {
		return method;
	}

	public boolean isPresent() {
		return method != null;
	}
}
