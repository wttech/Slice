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
package com.cognifide.slice.mapper.annotation

import com.cognifide.slice.test.setup.BaseSetup
import org.junit.Assert

/**
 * @author Krzysztof Watral
 */
class PostMappingTest extends BaseSetup {
	def "PostMapping Annotation Test"() {
		setup: "Creating initial content"
		pageBuilder.content {
			test("cq:PageContent") { "jcr:content"("field": "some value") }
		}

		when: "Get a model instance by path"
		PostMappingModel postMappingModel = modelProvider.get(PostMappingModel.class, "/content/test/jcr:content")

		then: "Model has been property initialized"
		Assert.assertNotNull(postMappingModel)
		Assert.assertEquals("Post-mapping annotation did not change object's state",
				"some value_TEST", postMappingModel.getField())
	}
}
