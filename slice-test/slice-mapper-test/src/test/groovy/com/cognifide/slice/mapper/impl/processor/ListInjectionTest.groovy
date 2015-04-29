/*-
 * #%L
 * Slice - Mapper Tests
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
package com.cognifide.slice.mapper.impl.processor

import com.cognifide.slice.mapper.annotation.ListInjectionModel
import com.cognifide.slice.test.setup.BaseSetup
import junit.framework.Assert

/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/23/15 3:26 PM
 */
class ListInjectionTest extends BaseSetup {

	def "Get a model with a List mapped from List"() {
		setup: "Create a content with String value"
		def path = "/content/foo/jcr:content"
		pageBuilder.content {
			foo("foo") {
				"jcr:content"("list": ["el1", "el2"])
			}
		}
		and: "Get a model"
		def model = modelProvider.get(ListInjectionModel.class, path)

		expect: "Model is not null"
		Assert.assertNotNull(model)

		and: "List is null"
		Assert.assertNotNull(model.getList())

		and: "List is not empty, it has exactly 2 items"
		Assert.assertEquals(2, model.getList().size())

		and: "List contains correct elements"
		Assert.assertEquals("el1", model.getList().get(0))
		Assert.assertEquals("el2", model.getList().get(1))
	}

	def "Get a model with a List mapped from Array"() {
		setup: "Create a content with String value"
		def path = "/content/foo/jcr:content"
		pageBuilder.content {
			foo("foo") {
				"jcr:content"("list": ["el1", "el2"].toArray(new String[0]))
			}
		}
		and: "Get a model"
		def model = modelProvider.get(ListInjectionModel.class, path)

		expect: "Model is not null"
		Assert.assertNotNull(model)

		and: "List is null"
		Assert.assertNotNull(model.getList())

		and: "List is not empty, it has exactly 2 items"
		Assert.assertEquals(2, model.getList().size())

		and: "List contains correct elements"
		Assert.assertEquals("el1", model.getList().get(0))
		Assert.assertEquals("el2", model.getList().get(1))
	}

	def "Get a model with a List mapped from String"() {
		setup: "Create a content with String value"
		def path = "/content/foo/jcr:content"
		pageBuilder.content {
			foo("foo") { "jcr:content"("list": "el1") }
		}
		and: "Get a model"
		def model = modelProvider.get(ListInjectionModel.class, path)

		expect: "Model is not null"
		Assert.assertNotNull(model)

		and: "List is null"
		Assert.assertNotNull(model.getList())

		and: "List is not empty, it has exactly 1 item"
		Assert.assertEquals(1, model.getList().size())

		and: "List contains correct element"
		Assert.assertEquals("el1", model.getList().get(0))
	}

	def "Get a model with a List mapped from null"() {
		setup: "Create a content with String value"
		def path = "/content/foo/jcr:content"
		pageBuilder.content {
			foo("foo") { "jcr:content"("list": null) }
		}
		and: "Get a model"
		def model = modelProvider.get(ListInjectionModel.class, path)

		expect: "Model is not null"
		Assert.assertNotNull(model)

		and: "List is null"
		Assert.assertNull(model.getList())
	}

	def cleanup() {
		// optionally call the method below to remove all test content after each feature method
		removeAllNodes()
	}
}
