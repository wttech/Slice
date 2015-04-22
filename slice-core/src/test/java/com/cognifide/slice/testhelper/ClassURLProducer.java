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

import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Mariusz Kubiś Date: 22.04.15
 */
public class ClassURLProducer {

	private static final Logger LOG = LoggerFactory.getLogger(ClassURLProducer.class);

	private String moduleName;

	private Set<String> classPaths;

	public ClassURLProducer(String moduleName, Set<String> classPaths) {
		this.moduleName = moduleName;
		this.classPaths = classPaths;
	}

	public Set<URL> getUrls() {
		String absolutePath = new File("").getAbsolutePath();

		String rootPath = absolutePath;
		if (!absolutePath.endsWith(moduleName)) {
			rootPath = absolutePath + "/" + moduleName;
		}

		System.out.println("absolutePath : " + absolutePath);

		Set<URL> urlSet = new LinkedHashSet<URL>();
		for (String classPath : classPaths) {
			URL url = null;
			try {
				url = new File(Joiner.on("/").join(rootPath, "target/test-classes", classPath) + ".class")
						.toURI().toURL();
				urlSet.add(url);
			} catch (MalformedURLException e) {
				LOG.error("Can not read class: " + classPath, e);
			}
		}
		return urlSet;
	}
}
