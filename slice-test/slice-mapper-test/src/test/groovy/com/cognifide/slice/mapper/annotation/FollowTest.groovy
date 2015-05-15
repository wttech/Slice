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

import org.junit.Assert

import com.cognifide.slice.test.setup.BaseSetup

/**
 * @author Mariusz Kubiś
 * Date: 10.04.15
 */
class FollowTest extends BaseSetup {

	def "Follow test"() {
		setup:
		String jcrPropertyModelPath = "/content/foo/jcr:content";

		pageBuilder.content {
			foo("cq:PageContent") {
				"jcr:content"("text": "Test", "style": "Style", "size": 5)
			}
		}
		pageBuilder.content {
			bar("cq:PageContent") { "jcr:content"("jcrPropertyModel": jcrPropertyModelPath) }
		}

		expect:
		assertPageExists("/content/foo")
		JcrPropertyModel jcrPropertyModel = modelProvider.get(JcrPropertyModel.class, jcrPropertyModelPath)
		checkJcrPropertyModel(jcrPropertyModel)

		assertPageExists("/content/bar")
		FollowModel followModel = modelProvider.get(FollowModel.class, "/content/bar/jcr:content")
		Assert.assertEquals(followModel.getJcrPropertyModelPath(), jcrPropertyModelPath)
		checkJcrPropertyModel(followModel.getJcrPropertyModel())
	}

	def "Follow test with null value"() {
		setup: "Create a test content, under path '/content/bar', with property 'jcrPropertyModel' set to null"
		pageBuilder.content {
			bar("cq:PageContent") { "jcr:content"("jcrPropertyModel": null) }
		}
		when: "Getting Follow model, based on null path"
		FollowModel followModel = modelProvider.get(FollowModel.class, "/content/bar/jcr:content")

		then: "All fields in follow model are null"
		Assert.assertNull(followModel.getJcrPropertyModel())
		Assert.assertNull(followModel.getJcrPropertyModelPath())
	}

	def "Follow test with non-string value"() {
		setup: "Create a test content, under path '/content/bar', with property 'jcrPropertyModel' set to null"
		pageBuilder.content {
			bar("cq:PageContent") { "jcr:content"("jcrPropertyModel": true) }
		}
		when: "Getting Follow model, based on non-string property"
		FollowModel followModel = modelProvider.get(FollowModel.class, "/content/bar/jcr:content")

		then: "Field with @Follow annotation is null"
		Assert.assertNull(followModel.getJcrPropertyModel())
		Assert.assertEquals("true", followModel.getJcrPropertyModelPath())
	}

	def "Follow test with non-existing follow resource"() {
		setup:
		pageBuilder.content {
			non_existingResource("cq:PageContent") { "jcr:content"("jcrPropertyModel": "/content/test1/jcr:content") }
		}

		expect:
		JcrPropertyModel jcrPropertyModel = modelProvider.get(JcrPropertyModel.class, "/content/test1/jcr:content")
		Assert.assertNotNull(jcrPropertyModel)
		Assert.assertNull(jcrPropertyModel.getText())
		Assert.assertNull(jcrPropertyModel.getSecondProperty())
		Assert.assertEquals(jcrPropertyModel.getSize(), 0)

		assertPageExists("/content/non_existingResource")
		FollowModel followModel = modelProvider.get(FollowModel.class, "/content/non_existingResource/jcr:content")
		Assert.assertNull(followModel.getJcrPropertyModel())
		Assert.assertEquals("/content/test1/jcr:content", followModel.getJcrPropertyModelPath())
	}

	def "Follow test with empty followed resource"() {
		given: "Followed property points a non-exisitng resource"
		pageBuilder.content {
			test("cq:PageContent") { "jcr:content"("jcrPropertyModel": "/content/emptyResource/jcr:content") }
		}
		pageBuilder.content {
			emptyResource("cq:PageContent") { "jcr:content"() }
		}
		assertPageExists("/content/emptyResource")
		assertPageExists("/content/test")

		when: "Getting model"
		FollowModel model = modelProvider.get(FollowModel.class, "/content/test/jcr:content")
		then: "Model is mapped"
		Assert.assertNotNull(model)
		and: "Followed model is not null"
		Assert.assertNotNull(model.getJcrPropertyModel())
		and: "And its properties are not set"
		Assert.assertNull(model.getJcrPropertyModel().getText())
		Assert.assertEquals(0, model.getJcrPropertyModel().getSize())
	}

	private static void checkJcrPropertyModel(JcrPropertyModel model) {
		Assert.assertNotNull(model)
		Assert.assertEquals(model.getText(), "Test")
		Assert.assertEquals(model.getSecondProperty(), "Style")
		Assert.assertEquals(model.getSize(), 5)
	}
}
