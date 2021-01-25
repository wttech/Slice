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
import com.google.inject.ProvisionException
import org.junit.Assert

/**
 * @author Mariusz Kubiś Date: 14.04.15
 */
class SliceReferenceTest extends BaseSetup {

	def setupSpec() {
		setup: "Create test content, under '/content/foo, with two properties: 'text' and 'style'"
		pageBuilder.content {
			foo("cq:PageContent") {
				"jcr:content"("text": "Test1", "style": "Style", "size": 5)
			}
		}
		and: "Create a test content, under '/content/bar', with property 'text'"
		pageBuilder.content {
			bar("cq:PageContent") { "jcr:content"("text": "Test") }
		}
	}

	def "Slice Reference test"() {
		when: "Get a model"
		SliceReferenceModel sliceReferenceModel = modelProvider.get(SliceReferenceModel.class, "/content/bar/jcr:content")
		and: "Get a property model from Slice Reference Model"
		JcrPropertyModel propertyModel = sliceReferenceModel.getPropertyModel()

		then: "Model is not null"
		Assert.assertNotNull(sliceReferenceModel)

		and: "Referenced Model is not null"
		Assert.assertNotNull(propertyModel)

		and: "Model has a correct value for property 'text'"
		Assert.assertEquals("Test", sliceReferenceModel.getText())

		and: "Referenced Model has a correct value for a property 'text'"
		Assert.assertEquals("Test1", propertyModel.getText())

		and: "Referenced Model has a correct value for a property 'secondProperty'"
		Assert.assertEquals("Style", propertyModel.getSecondProperty())

		and: "Referenced Model has a correct value for a property 'size'"
		Assert.assertEquals(5, propertyModel.getSize())
	}

	def "Slice Reference test for missing referenced resource"() {
		when: "Get a model"
		SliceReferenceWithNoReferencedResource sliceModel = modelProvider.get(SliceReferenceWithNoReferencedResource.class, "/content/bar/jcr:content")
		and: "Get a property model from Slice Reference Model"
		JcrPropertyModel referencedModel = sliceModel.getPropertyModel()

		then: "Model is not null"
		Assert.assertNotNull(sliceModel)

		and: "Referenced Model is null"
		Assert.assertNotNull(referencedModel)
		
		and: "Referenced Model has a correct value for a property 'text'"
		Assert.assertNull(referencedModel.getText())

		and: "Referenced Model has a correct value for a property 'secondProperty'"
		Assert.assertNull(referencedModel.getSecondProperty())

		and: "Referenced Model has a correct value for a property 'size'"
		Assert.assertEquals(0, referencedModel.getSize())

		and: "Model has a correct value for property 'text'"
		Assert.assertEquals("Test", sliceModel.getText())
	}

	def "Slice Reference with empty path"() {
		def path = "/content/foo/jcr:content"
		when: "Get a model with empty Slice Reference path"
		modelProvider.get(EmptySliceReferenceModel.class, path)

		then: "Provision Exception is thrown"
		thrown(ProvisionException)
	}

	def "Slice Reference with empty placeholder path"() {
		def path = "/content/foo/jcr:content"
		when: "Get a model with empty placeholder reference path"
		modelProvider.get(SliceReferenceModelWithEmptyPlaceholderReference.class, path)

		then: "Provision Exception is thrown"
		thrown(ProvisionException)
	}

	def "Slice Reference with nested path"() {
		def path = "/content/foo";
		setup: "Create a content"
		pageBuilder.content {
			foo("cq:PageContent") {
				"jcr:content"() {
					"propertyModel"("text": "Test1", "style": "Style", "size": 5)
				}
			}
		}
		and: "Get a model"
		SliceReferenceRelativePathModel sliceReferenceModel = modelProvider.get(SliceReferenceRelativePathModel.class, path + "/jcr:content")
		and: "Get a property model from Slice Reference Model"
		JcrPropertyModel propertyModel = sliceReferenceModel.getPropertyModel()

		expect: "Model is not null"
		Assert.assertNotNull(sliceReferenceModel)

		and: "Referenced Model is not null"
		Assert.assertNotNull(propertyModel)

		and: "Referenced Model has a correct value for a property 'text'"
		Assert.assertEquals("Test1", propertyModel.getText())

		and: "Referenced Model has a correct value for a property 'secondProperty'"
		Assert.assertEquals("Style", propertyModel.getSecondProperty())

		and: "Referenced Model has a correct value for a property 'size'"
		Assert.assertEquals(5, propertyModel.getSize())
	}
}
