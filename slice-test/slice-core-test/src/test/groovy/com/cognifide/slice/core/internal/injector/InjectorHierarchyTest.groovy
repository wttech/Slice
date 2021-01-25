/*-
 * #%L
 * Slice - Core Tests
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
package com.cognifide.slice.core.internal.injector

import com.google.inject.Injector
import org.junit.Assert

/**
 * @author Jaromir Celejewski
 * Date: 10.04.15
 */
class InjectorHierarchyTest extends InjectorsTestSetup{

	def "Find injector by name"() {
		given: "defined injectors: 'slice-test', 'slice-test2', 'slice-test/subtest'"

		when: "fetching different injectors"
		Injector injector = injectorHierarchy.getInjectorByName("slice-test")
		Injector injector2 = injectorHierarchy.getInjectorByName("slice-test2")
		Injector injector3 = injectorHierarchy.getInjectorByName("slice-test/subtest")

		then: "'slice-test', 'slice-test2', 'slice-test/subtest' exist in injector's hierarchy"
		Assert.assertNotNull(injector)
		Assert.assertNotNull(injector2)
		Assert.assertNotNull(injector3)
	}

	def "Can't find injector by name if it is not created"() {
		given: "defined injectors: 'slice-test', 'slice-test2', 'slice-test/subtest'"

		when: "fetching non-exisitng injector"
		Injector injectorNonExistent = injectorHierarchy.getInjectorByName("slice-test3")

		then: "injector does not exist in hierarchy"
		Assert.assertNull(injectorNonExistent)
	}

	def "Can't find injector by null name"() {
		given: "defined injectors: 'slice-test', 'slice-test2', 'slice-test/subtest'"

		when: "fetching null injector"
		Injector injectorNonExistent = injectorHierarchy.getInjectorByName(null)

		then: "injector with null name does not exist in hierarchy"
		Assert.assertNull(injectorNonExistent)
	}

	def "Find injector by application path"() {
		given: "defined injectors: 'slice-test', 'slice-test2', 'slice-test/subtest'"

		when: "fetching names by application path"
		String injector1name = injectorHierarchy.getInjectorNameByApplicationPath("/apps/slice-test")
		String injector2name = injectorHierarchy.getInjectorNameByApplicationPath("/apps/slice-test2")
		String injector3name = injectorHierarchy.getInjectorNameByApplicationPath("/apps/slice-test/subtest")

		then: "appropriate injector names can be found in injector's hierarchy"
		Assert.assertEquals("slice-test", injector1name)
		Assert.assertEquals("slice-test2", injector2name)
		Assert.assertEquals("slice-test/subtest", injector3name)
	}

	def "Can't find injector by application path if it is not registered"() {
		given: "defined injectors: 'slice-test', 'slice-test2', 'slice-test/subtest'"

		when: "fetching injector for apps which are not registered"
		String injector1name = injectorHierarchy.getInjectorNameByApplicationPath("/apps/slice-test3")
		String injector2name = injectorHierarchy.getInjectorNameByApplicationPath("/apps/slice-test/subtest2")
		String injectorForNullName = injectorHierarchy.getInjectorNameByApplicationPath(null)

		then: "injectors cannot be found in injector's hierarchy"
		Assert.assertNull(injector1name)
		Assert.assertNull(injector2name)
		Assert.assertNull(injectorForNullName)
	}

	def "Can find injector by its registered name"() {
		given: "defined injectors: 'slice-test', 'slice-test2', 'slice-test/subtest'"

		when:
		Injector injector1 = injectorHierarchy.getInjectorByName("slice-test")
		String injector1regName = injectorHierarchy.getRegisteredName(injector1)
		Injector injector2 = injectorHierarchy.getInjectorByName("slice-test2")
		String injector2regName = injectorHierarchy.getRegisteredName(injector2)
		Injector injector3 = injectorHierarchy.getInjectorByName("slice-test/subtest")
		String injector3regName = injectorHierarchy.getRegisteredName(injector3)

		then: "names of all three given injectors can be found in the hierarchy"
		Assert.assertEquals("slice-test", injector1regName)
		Assert.assertEquals("slice-test2", injector2regName)
		Assert.assertEquals("slice-test/subtest", injector3regName)
	}

	def "Collection of names in hierarchy contains only registered injectors"() {
		given: "defined injectors: 'slice-test', 'slice-test2', 'slice-test/subtest'"

		expect: "collection of injector's names contains: 'slice-test', 'slice-test2', 'slice-test/subtest'"
		Assert.assertTrue(injectorHierarchy.getInjectorNames().contains("slice-test"))
		Assert.assertTrue(injectorHierarchy.getInjectorNames().contains("slice-test2"))
		Assert.assertTrue(injectorHierarchy.getInjectorNames().contains("slice-test/subtest"))
		Assert.assertFalse(injectorHierarchy.getInjectorNames().contains("slice-test/subtestx"))
		Assert.assertFalse(injectorHierarchy.getInjectorNames().contains(null))
	}
}

