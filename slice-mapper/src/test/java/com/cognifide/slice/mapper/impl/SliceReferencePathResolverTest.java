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

package com.cognifide.slice.mapper.impl;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import com.cognifide.slice.mapper.api.SliceReferencePathResolver;
import com.google.inject.Injector;
import com.google.inject.Key;

public class SliceReferencePathResolverTest {

	@Test
	public void testResolvePlaceholdersInPath() {
		SliceReferencePathResolver pathResolver = new SliceReferencePathResolverImpl(null);
		pathResolver.addPlaceholder("globalConfig", "/content/test/home/global-config");
		String resolvedPath = pathResolver.resolvePlaceholdersInPath("${globalConfig}/jcr:content/test");
		Assert.assertEquals("/content/test/home/global-config/jcr:content/test", resolvedPath);
	}

	@Test
	public void testResolvePlaceholdersInPathAllSigns() {
		SliceReferencePathResolver pathResolver = new SliceReferencePathResolverImpl(null);
		pathResolver.addPlaceholder("global-0Config_", "/content/test/home/global-config");
		String resolvedPath = pathResolver.resolvePlaceholdersInPath("${global-0Config_}/jcr:content/test");
		Assert.assertEquals("/content/test/home/global-config/jcr:content/test", resolvedPath);
	}

	@Test
	public void testResolvePlaceholdersInPathMultiString() {
		SliceReferencePathResolver pathResolver = new SliceReferencePathResolverImpl(null);
		pathResolver.addPlaceholder("lg", "en");
		pathResolver.addPlaceholder("app", "/content/test");
		pathResolver.addPlaceholder("home", "home");
		String resolvedPath = pathResolver.resolvePlaceholdersInPath("${app}/${lg}/${home}.html");
		Assert.assertEquals("/content/test/en/home.html", resolvedPath);
	}

	@Test
	public void testResolvePlaceholdersInPathSimpleAnnotation() {
		Injector injector = Mockito.mock(Injector.class);
		Key<String> key = Key.get(String.class, TestHomePath.class);
		Mockito.when(injector.getInstance(key)).thenReturn("/content/test/en/home");
		SliceReferencePathResolver pathResolver = new SliceReferencePathResolverImpl(injector);
		pathResolver.addPlaceholder("home", TestHomePath.class);

		String resolvedPath = pathResolver.resolvePlaceholdersInPath("${home}.html");
		Assert.assertEquals("/content/test/en/home.html", resolvedPath);
	}

	@Test
	public void testResolvePlaceholdersInPathAnnotationPlusString() {
		Injector injector = Mockito.mock(Injector.class);
		Key<String> key = Key.get(String.class, TestHomePath.class);
		Mockito.when(injector.getInstance(key)).thenReturn("home");
		SliceReferencePathResolver pathResolver = new SliceReferencePathResolverImpl(injector);
		pathResolver.addPlaceholder("home", TestHomePath.class);
		pathResolver.addPlaceholder("app", "/content/test/en");

		String resolvedPath = pathResolver.resolvePlaceholdersInPath("${app}/${home}.html");
		Assert.assertEquals("/content/test/en/home.html", resolvedPath);
	}
}
