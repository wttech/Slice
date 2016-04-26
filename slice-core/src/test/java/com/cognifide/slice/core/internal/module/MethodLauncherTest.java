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

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.cognifide.slice.mapper.annotation.PostMapping;
import com.cognifide.slice.mapper.annotation.PreMapping;

@PrepareForTest({MethodLauncher.class})
@RunWith(PowerMockRunner.class)
public class MethodLauncherTest {

	@Test
	public void testMethodLauncher() throws Exception {
		MethodLauncher methodLauncher = spy(MethodLauncher.INSTANCE);
		Map<MethodLauncherCacheKey, MethodWrapper> cache = Whitebox.getInternalState(methodLauncher, Map.class);

		AnnotatedModel first = new AnnotatedModel();
		AnnotatedModel second = new AnnotatedModel();
		AnnotatedModel third = new AnnotatedModel();

		NotAnnotatedModel notAnnotated = new NotAnnotatedModel();

		methodLauncher.invokeMethodFor(PreMapping.class, first);
		methodLauncher.invokeMethodFor(PreMapping.class, second);
		methodLauncher.invokeMethodFor(PreMapping.class, third);

		methodLauncher.invokeMethodFor(PostMapping.class, first);
		methodLauncher.invokeMethodFor(PostMapping.class, second);
		methodLauncher.invokeMethodFor(PostMapping.class, third);

		methodLauncher.invokeMethodFor(PreMapping.class, notAnnotated);
		methodLauncher.invokeMethodFor(PostMapping.class, notAnnotated);

		// verify findMethod is called once per Class/Annotation pair
		verifyPrivate(methodLauncher, times(1)).invoke("findMethod", AnnotatedModel.class, PreMapping.class);
		verifyPrivate(methodLauncher, times(1)).invoke("findMethod", AnnotatedModel.class, PostMapping.class);
		verifyPrivate(methodLauncher, times(1)).invoke("findMethod", NotAnnotatedModel.class, PreMapping.class);
		verifyPrivate(methodLauncher, times(1)).invoke("findMethod", NotAnnotatedModel.class, PostMapping.class);

		// verify cache size
		assertEquals(4, cache.size());

		// verify cache contains proper keys
		MethodLauncherCacheKey annotatedClassKey = new MethodLauncherCacheKey(PreMapping.class, AnnotatedModel.class);
		assertEquals(true, cache.keySet().contains(annotatedClassKey));
		assertEquals(true, cache.keySet().contains(new MethodLauncherCacheKey(PostMapping.class, AnnotatedModel.class)));

		MethodLauncherCacheKey notAnnotatedClassKey = new MethodLauncherCacheKey(PreMapping.class, NotAnnotatedModel.class);
		assertEquals(true, cache.keySet().contains(notAnnotatedClassKey));
		assertEquals(true, cache.keySet().contains(new MethodLauncherCacheKey(PostMapping.class, NotAnnotatedModel.class)));

		// verify if cache contains proper values
		assertEquals(true, cache.get(annotatedClassKey).isPresent());
		assertEquals(false, cache.get(notAnnotatedClassKey).isPresent());
	}

	class AnnotatedModel {

		@PreMapping
		void preMapping() { }

		@PostMapping
		void postMapping() { }
	}

	class NotAnnotatedModel { }

}