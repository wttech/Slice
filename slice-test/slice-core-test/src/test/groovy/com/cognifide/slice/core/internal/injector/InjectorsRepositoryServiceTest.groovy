/*-
 * #%L
 * Slice - Core Tests
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
package com.cognifide.slice.core.internal.injector

import com.cognifide.slice.api.injector.InjectorWithContext
import org.junit.Assert

/**
 * Created by T530 on 2015-04-21.
 */
class InjectorsRepositoryServiceTest extends InjectorsTestSetup {

	def "Find name of proper injector for given resource path"() {
		given: "defined injectors: 'slice-test', 'slice-test2', 'slice-test/subtest'"
		List<String> injectors = Arrays.asList("slice-test2", "slice-test", "slice-test/subtest")

		when:
		String injectorNameForRes1 = repositoryService.getInjectorNameForResource("slice-test/abc/abc")
		String injectorNameForRes2 = repositoryService.getInjectorNameForResource("/apps/slice-test/abc/abc")
		String injectorNameForRes3 = repositoryService.getInjectorNameForResource("slice-test/subtest/abc")
		String injectorNameForRes4 = repositoryService.getInjectorNameForResource("slice-test3/subtest/abc")
		String injectorNameForResNull = repositoryService.getInjectorNameForResource(null)

		then: "find best matching injector for given resource path or return null if no matching injector found"
		Assert.assertTrue(injectors.containsAll(repositoryService.getInjectorNames()) &&
				repositoryService.getInjectorNames().containsAll(injectors))
		Assert.assertEquals("slice-test", injectorNameForRes1)
		Assert.assertEquals("slice-test", injectorNameForRes2)
		Assert.assertEquals("slice-test/subtest", injectorNameForRes3)
		Assert.assertNull(injectorNameForRes4)
		Assert.assertNull(injectorNameForResNull)
	}

	def "Get InjectorWithContext for given injector name"() {
		given: "defined injectors: 'slice-test', 'slice-test2', 'slice-test/subtest'"

		when:
		InjectorWithContext injectorWithContext1 = repositoryService.getInjector("slice-test/subtest")
		InjectorWithContext injectorWithContext2 = repositoryService.getInjector("slice-test")
		InjectorWithContext injectorWithContext3 = repositoryService.getInjector("slice-test2")
		InjectorWithContext injectorWithContext4 = repositoryService.getInjector("slice-test/subtestx")
		InjectorWithContext injectorWithContext5 = repositoryService.getInjector(null)

		then: "return non null InjectorWithContext whenever injector with a given name exists"
		Assert.assertNotNull(injectorWithContext1)
		Assert.assertNotNull(injectorWithContext2)
		Assert.assertNotNull(injectorWithContext3)
		Assert.assertNull(injectorWithContext4)
		Assert.assertNull(injectorWithContext5)
	}
}
