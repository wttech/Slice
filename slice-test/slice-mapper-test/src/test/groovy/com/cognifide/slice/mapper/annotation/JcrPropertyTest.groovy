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
 * @author Mariusz Kubiś
 * Date: 10.04.15
 */
class JcrPropertyTest extends BaseSetup {

	def "Jcr properties test"() {
		setup:
		pageBuilder.content {
			foo("foo") {
				"jcr:content"("text": "Test", "style": "Style", "size": 5)
			}
		}
		expect:
		assertPageExists("/content/foo")
		JcrPropertyModel jcrPropertyModel = modelProvider.get(JcrPropertyModel.class, "/content/foo/jcr:content")
		Assert.assertNotNull(jcrPropertyModel)
		Assert.assertEquals(jcrPropertyModel.getText(), "Test")
		Assert.assertEquals(jcrPropertyModel.getSecondProperty(), "Style")
		Assert.assertEquals(jcrPropertyModel.getSize(), 5)
		Assert.assertEquals(jcrPropertyModel.getSizeFinal(), 0)
		Assert.assertEquals(jcrPropertyModel.getSizeStatic(), 0)
	}
}
