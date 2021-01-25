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
package com.cognifide.slice.api.qualifier

import com.cognifide.slice.core.internal.injector.InjectorsTestSetup
import com.google.inject.Injector
import junit.framework.Assert
import spock.lang.Shared


/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/22/15 1:58 PM
 */
class InjectorNameInjectionTest extends InjectorsTestSetup {

	@Shared
	Injector injector

	/**
	 * Since RepositoryService is a OSGi service and it's being injected by Peaberry, test invokes getInjectorName method
	 * directly.
	 */
	def "Get Injector Name"() {
		setup: "Get Injector name from SliceModule"
		injector = injectorHierarchy.getInjectorByName("slice-test")
		def injectorName = sliceModule.getInjectorName(repositoryService, injector)

		expect: "Injector name is not null"
		Assert.assertNotNull(injectorName)

		and: "Injector name is equal to 'injector-name'"
		Assert.assertEquals("slice-test", injectorName)
	}
}
