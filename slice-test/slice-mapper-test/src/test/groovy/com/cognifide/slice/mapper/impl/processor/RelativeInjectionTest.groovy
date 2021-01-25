/*-
 * #%L
 * Slice - Mapper Tests
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
package com.cognifide.slice.mapper.impl.processor

import junit.framework.Assert

import com.cognifide.slice.mapper.annotation.ChildModel
import com.cognifide.slice.test.setup.BaseSetup


class RelativeInjectionTest extends BaseSetup {

	def "Get a model with a Enum mapped from String - existing Enum value"() {
		setup: "Create a content with String value (existing Enum value)"
		def path = "/content/foo/jcr:content/child"
		pageBuilder.content {
			foo("foo") { "jcr:content"("parentProp": "bar") {
				"child"("prop1": "VALUE1")
				}
			}
		}
		and: "Get a model"
		def model = modelProvider.get(ChildModel.class, path)

		expect: "Model is not null"
		Assert.assertNotNull(model)

		and: "Parent model is correctly mapped"
		Assert.assertEquals("bar", model.getParentProp())
	}
}
