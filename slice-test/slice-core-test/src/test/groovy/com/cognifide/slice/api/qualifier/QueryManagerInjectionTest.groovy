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

import com.google.inject.Key
import junit.framework.Assert

import javax.jcr.query.QueryManager

/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/22/15 3:39 PM
 */
class QueryManagerInjectionTest extends SlingUrlTestBase {

	def "Get Query Manager"() {
		setup: "Initialize context with a filled request"
		richRequestContext()
		and: "Get Query Manager from injector"
		def queryManager = injector.getInstance(Key.get(QueryManager.class))

		expect: "Query Manager is not null"
		Assert.assertNotNull(queryManager)

		and: "Query Manager is an instance of a QueryManager"
		Assert.assertTrue(queryManager instanceof QueryManager)
	}

	def "Get Query Manager - empty request"() {
		setup: "Initialize context with an empty request"
		emptyRequestContext()
		and: "Get Query Manager from injector"
		def queryManager = injector.getInstance(Key.get(QueryManager.class))

		expect: "Query Manager is not null"
		Assert.assertNotNull(queryManager)

		and: "Query Manager is an instance of a QueryManager"
		Assert.assertTrue(queryManager instanceof QueryManager)
	}
}