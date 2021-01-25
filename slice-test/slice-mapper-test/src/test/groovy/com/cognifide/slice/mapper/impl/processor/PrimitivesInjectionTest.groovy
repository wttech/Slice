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

import com.cognifide.slice.mapper.annotation.BooleanInjectionModel
import com.cognifide.slice.mapper.annotation.PrimitivesModel
import com.cognifide.slice.test.setup.BaseSetup
import junit.framework.Assert

class PrimitivesInjectionTest extends BaseSetup {

	def "Mapping primitives from existing properties"() {
		given: "Content contains all properties"
		def path = "/content/foo/jcr:content"
		pageBuilder.content {
			foo("foo") {
				"jcr:content"(
						"boolProperty": "true",
						"intProperty": 2,
						"longProperty": 2.3,
						)
			}
		}

		when: "Getting a model"
		PrimitivesModel model = modelProvider.get(PrimitivesModel.class, path)

		then: "Model is not null"
		Assert.assertNotNull(model)
		and: "All primitives are set"
		Assert.assertEquals(Boolean.TRUE, model.isBoolProperty())
		Assert.assertEquals(2, model.getIntProperty())
		Assert.assertEquals(2, model.getLongProperty())
		Assert.assertEquals(2.3, model.getFloatProperty(), 2)
		Assert.assertEquals(2.3, model.getFloatProperty(), 2)
	}

	def "Mapping primitives from non-existing properties"() {
		given: "Content contains no expected properties"
		pageBuilder.content {
			bar { "jcr:content"( "someProperty": "Hi There!" ) }
		}

		when: "Getting a model"
		PrimitivesModel model = modelProvider.get(PrimitivesModel.class, "/content/bar/jcr:content")

		then: "Model is not null"
		Assert.assertNotNull(model)
		and: "All primitives are set to their default values"
		Assert.assertEquals(Boolean.FALSE, model.isBoolProperty())
		Assert.assertEquals(0, model.getIntProperty())
		Assert.assertEquals(0, model.getLongProperty())
		Assert.assertEquals(0.0, model.getFloatProperty(), 2)
		Assert.assertEquals(0.0, model.getFloatProperty(), 2)
	}
}