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
 * @author Mariusz Kubiś Date: 14.04.15
 */
class IgnorePropertyTest extends BaseSetup {

	def "Ignore property test for mapped All fields"() {
		setup:
		pageBuilder.content {
			foo("cq:PageContent") {
				"jcr:content"("text": "Test", "style": "Style", "size": 5, "label": "Label")
			}
		}

		when: "Mapping all fields"
		IgnorePropertyModelAll ignorePropertyModelAll = modelProvider.get(IgnorePropertyModelAll.class,
				"/content/foo/jcr:content")

		then: "model is mapped"
		Assert.assertNotNull(ignorePropertyModelAll)
		and: "not annotated field is mapped"
		Assert.assertEquals("Test", ignorePropertyModelAll.getText())
		and: "ignored field keeps value set in constructor"
		Assert.assertEquals(10, ignorePropertyModelAll.getSize())
		and: "ignored field keeps value set in field"
		Assert.assertEquals("default", ignorePropertyModelAll.getStyle())
		and: "ignored field without default value is null"
		Assert.assertNull(ignorePropertyModelAll.getLabel())
	}

	def "Ignore property test for mapped Annotated fields"() {
		setup:
		pageBuilder.content {
			foo("cq:PageContent") {
				"jcr:content"("text": "Test", "style": "Style", "size": 5, "label": "Label")
			}
		}

		when: "Mapping annotated field"
		IgnorePropertyModelAnnotated ignorePropertyModelAnnotated = modelProvider.get(IgnorePropertyModelAnnotated.class,
				"/content/foo/jcr:content")

		then: "model is mapped"
		Assert.assertNotNull(ignorePropertyModelAnnotated)
		and: "annotated field is mapped"
		Assert.assertEquals("Test", ignorePropertyModelAnnotated.getText())
		and: "not annotated field keeps its default value"
		Assert.assertEquals("default", ignorePropertyModelAnnotated.getStyle())
		and: "not annotated field keeps value set in constructor"
		Assert.assertEquals(10, ignorePropertyModelAnnotated.getSize())
		and: "ignored field is null"
		Assert.assertNull(ignorePropertyModelAnnotated.getLabel())
	}
}
