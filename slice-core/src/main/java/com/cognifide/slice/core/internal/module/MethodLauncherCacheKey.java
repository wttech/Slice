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

import java.lang.annotation.Annotation;

class MethodLauncherCacheKey {

	private final Class<? extends Annotation> annotation;

	private final Class<?> injectee;

	MethodLauncherCacheKey(Class<? extends Annotation> annotation, Class<?> injectee) {
		this.annotation = annotation;
		this.injectee = injectee;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MethodLauncherCacheKey key = (MethodLauncherCacheKey) o;
		return annotation.equals(key.annotation) && injectee.equals(key.injectee);
	}

	@Override
	public int hashCode() {
		int result = annotation.hashCode();
		return 31 * result + injectee.hashCode();
	}
}
