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
package com.cognifide.slice.core.internal.adapter

import com.cognifide.slice.test.module.SimpleModel
import junit.framework.Assert


/**
 * Created by Jaromir Celejewski on 2015-04-23.
 */
class SliceAdapterFactoryTest extends AdapterBaseSetup {

	def setupSpec() {
		setup: "Create simple, initial content - node 'foo' with property 'prop1' set to 'prop1Value'"
		pageBuilder.content {
			foo("foo") { "jcr:content"("prop1": "prop1Value") }
		}
	}

	def "resource is adaptable to multiple types without RequestContextProvider"() {
		setup:
		def path = "/content/foo/jcr:content"
		def resource = resourceResolver.getResource(path)
		def SimpleModel model = resource.adaptTo(SimpleModel.class)

		expect:
		Assert.assertNotNull(model)

		and:
		Assert.assertEquals("prop1Value", model.getProp1())
	}

	def "resource is adaptable to multiple types with RequestContextProvider"() {
		setup:
		injectPrivateField(injectorRepositoryAdapterFactory, createRequestContextProvider(), "requestContextProvider")

		def path = "/content/foo/jcr:content"
		def resource = resourceResolver.getResource(path)
		def SimpleModel model = resource.adaptTo(SimpleModel.class)

		expect:
		Assert.assertNotNull(model)

		and:
		Assert.assertEquals("prop1Value", model.getProp1())
	}
}