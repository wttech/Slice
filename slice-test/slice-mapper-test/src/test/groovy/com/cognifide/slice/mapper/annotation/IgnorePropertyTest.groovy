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

	def "Ignore property test"() {
		setup:
		pageBuilder.content {
			foo("cq:PageContent") {
				"jcr:content"("text": "Test", "style": "Style", "size": 5)
			}
		}
		def contentNode = session.getNode("/content/foo/jcr:content")
		IgnorePropertyModelAll ignorePropertyModelAll = modelProvider.get(IgnorePropertyModelAll.class,
				"/content/foo/jcr:content")
		IgnorePropertyModelAnnotated ignorePropertyModelAnnotated = modelProvider.get(IgnorePropertyModelAnnotated.class,
				"/content/foo/jcr:content")

		expect:
		Assert.assertEquals(contentNode.get("style"), "Style")
		Assert.assertEquals(contentNode.get("size"), 5)

		Assert.assertNotNull(ignorePropertyModelAll)
		Assert.assertEquals(ignorePropertyModelAll.getText(), "Test")
		Assert.assertEquals(ignorePropertyModelAll.getSize(), 10)
		Assert.assertNull(ignorePropertyModelAll.getStyle())

		Assert.assertNotNull(ignorePropertyModelAnnotated)
		Assert.assertEquals(ignorePropertyModelAnnotated.getText(), "Test")
		Assert.assertEquals(ignorePropertyModelAnnotated.getSize(), 10)
		Assert.assertNull(ignorePropertyModelAnnotated.getStyle())
	}
}
