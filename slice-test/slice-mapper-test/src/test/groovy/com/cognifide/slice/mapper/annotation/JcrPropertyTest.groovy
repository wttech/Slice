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
				"jcr:content"("text": "Test", "style": "Style", "size": 5, "sizeLong": 123, "sizeDouble": 1.2)
			}
		}
		expect:
		assertPageExists("/content/foo")
		JcrPropertyAdvancedModel jcrPropertyModel = modelProvider.get(JcrPropertyAdvancedModel.class, "/content/foo/jcr:content")
		Assert.assertNotNull(jcrPropertyModel)
		Assert.assertEquals("Test", jcrPropertyModel.getText())
		Assert.assertEquals("Style",jcrPropertyModel.getSecondProperty())
		Assert.assertEquals(5, jcrPropertyModel.getSize())
		Assert.assertEquals(2, jcrPropertyModel.getSizeFinal())
		Assert.assertEquals(4, jcrPropertyModel.getSizeStatic())
		Assert.assertEquals(123, jcrPropertyModel.getSizeLong())
		Assert.assertEquals(1.2, jcrPropertyModel.getSizeDouble(), 2)
		Assert.assertEquals(1.2, jcrPropertyModel.getSizeFloat(), 2)
		Assert.assertEquals(1.2, jcrPropertyModel.getSizeShort(), 2)
		Assert.assertEquals(123, jcrPropertyModel.getSizeLongObject())
		Assert.assertEquals(1.2, jcrPropertyModel.getSizeDoubleObject(), 2)
		Assert.assertEquals(1.2, jcrPropertyModel.getSizeFloatObject(), 2)
		Assert.assertEquals(1.2, jcrPropertyModel.getSizeShortObject(), 2)
	}
}
