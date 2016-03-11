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
package com.cognifide.slice.testhelper;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Mariusz Kubiś Date: 22.04.15
 */
public class ClassURLProducer {

	private Set<String> classPaths;

	public ClassURLProducer(Set<String> classPaths) {
		this.classPaths = classPaths;
	}

	public Set<URL> getUrls() {

		Set<URL> urlSet = new LinkedHashSet<URL>();
		for (String classPath : classPaths) {
			URL url = getClass().getClassLoader().getResource(classPath + ".class");
			if (url != null) {
				urlSet.add(url);
			}
		}
		return urlSet;
	}
}
