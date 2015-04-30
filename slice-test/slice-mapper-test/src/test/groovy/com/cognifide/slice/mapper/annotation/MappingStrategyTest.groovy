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
 * @author Mariusz Kubiś Date: 15.04.15
 */
class MappingStrategyTest extends BaseSetup {

	def setup() {
		pageBuilder.content {
			foo("cq:PageContent") {
				"jcr:content"("field1": "field1 value", "field2": "field2 value", "field3": "field3 value")
			}
		}
	}

	def "Mapping Strategy All Test"() {
		expect:
		MappingStrategyAllModel mappingStrategyAllModel = modelProvider.get(MappingStrategyAllModel.class, "/content/foo/jcr:content")
		Assert.assertNotNull(mappingStrategyAllModel)
		Assert.assertEquals("field1 value", mappingStrategyAllModel.getField1())
		Assert.assertEquals("field2 value", mappingStrategyAllModel.getField2())
		Assert.assertEquals( "field3 value", mappingStrategyAllModel.getField3())
	}

	def "Mapping Strategy Annotated Test"() {
		expect:
		MappingStrategyAnnotatedModel mappingStrategyAnnotatedModel = modelProvider.get(MappingStrategyAnnotatedModel.class, "/content/foo/jcr:content")
		Assert.assertNotNull(mappingStrategyAnnotatedModel)
		Assert.assertEquals("field1 value", mappingStrategyAnnotatedModel.getField1())
		Assert.assertNull(mappingStrategyAnnotatedModel.getField2())
		Assert.assertEquals("field3 value", mappingStrategyAnnotatedModel.getField3())
	}
}
