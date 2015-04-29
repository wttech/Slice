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
class InheritanceTest extends BaseSetup {

	def "Inheritance test: Setup test content"() {
		setup:
		pageBuilder.content {
			foo("cq:PageContent") {
				"jcr:content"("field1": "field1 value", "field2": "field2 value", "field3": "field3 value")
			}
		}
		def contentNode = session.getNode("/content/foo/jcr:content")
		expect:
		Assert.assertEquals(contentNode.get("field1"), "field1 value")
		Assert.assertEquals(contentNode.get("field2"), "field2 value")
	}

	def "Inheritance test: Child model with MappingStrategy.ANNOTATED, parent model with MappingStrategy.ANNOTATED"() {
		expect:
		InheritanceModelAnnotated inheritanceModel = modelProvider.get(InheritanceModelAnnotated.class, "/content/foo/jcr:content")
		Assert.assertNotNull(inheritanceModel)
		Assert.assertEquals(inheritanceModel.getField1(), "field1 value")
		Assert.assertEquals(inheritanceModel.getField2(), "field2 value")
		Assert.assertNull(inheritanceModel.getField3())
	}

	def "Inheritance test: Child model with MappingStrategy.ANNOTATED, parent model with MappingStrategy.ALL"() {
		expect:
		InheritanceModelAnnotatedAll inheritanceModel = modelProvider.get(InheritanceModelAnnotatedAll.class, "/content/foo/jcr:content")
		Assert.assertNotNull(inheritanceModel)
		Assert.assertEquals(inheritanceModel.getField1(), "field1 value")
		Assert.assertEquals(inheritanceModel.getField2(), "field2 value")
		Assert.assertNull(inheritanceModel.getField3())
	}

	def "Inheritance test: Child model with MappingStrategy.ANNOTATED, parent model without MappingStrategy"() {
		expect:
		InheritanceModelAnnotatedWithout inheritanceModel = modelProvider.get(InheritanceModelAnnotatedWithout.class, "/content/foo/jcr:content")
		Assert.assertNotNull(inheritanceModel)
		Assert.assertNull(inheritanceModel.getField1())
		Assert.assertEquals(inheritanceModel.getField2(), "field2 value")
		Assert.assertNull(inheritanceModel.getField3())
	}

	def "Inheritance test: Child model with MappingStrategy.ALL, parent model with MappingStrategy.ALL"() {
		expect:
		InheritanceModelAll inheritanceModel = modelProvider.get(InheritanceModelAll.class, "/content/foo/jcr:content")
		Assert.assertNotNull(inheritanceModel)
		Assert.assertEquals(inheritanceModel.getField1(), "field1 value")
		Assert.assertEquals(inheritanceModel.getField2(), "field2 value")
	}

	def "Inheritance test: Child model with MappingStrategy.ALL, parent model with MappingStrategy.ANNOTATED"() {
		expect:
		InheritanceModelAllAnnotated inheritanceModel = modelProvider.get(InheritanceModelAllAnnotated.class, "/content/foo/jcr:content")
		Assert.assertNotNull(inheritanceModel)
		Assert.assertEquals(inheritanceModel.getField1(), "field1 value")
		Assert.assertEquals(inheritanceModel.getField2(), "field2 value")
	}

	def "Inheritance test: Child model with MappingStrategy.ALL, parent model without MappingStrategy"() {
		expect:
		InheritanceModelAllWithout inheritanceModel = modelProvider.get(InheritanceModelAllWithout.class, "/content/foo/jcr:content")
		Assert.assertNotNull(inheritanceModel)
		Assert.assertNull(inheritanceModel.getField1())
		Assert.assertEquals(inheritanceModel.getField2(), "field2 value")
	}

	def "Inheritance test: Child model without MappingStrategy, parent model with MappingStrategy.ALL"() {
		expect:
		InheritanceModelWithoutAll inheritanceModel = modelProvider.get(InheritanceModelWithoutAll.class, "/content/foo/jcr:content")
		Assert.assertNotNull(inheritanceModel)
		Assert.assertNull(inheritanceModel.getField1())
		Assert.assertNull(inheritanceModel.getField2())
	}

	def "Inheritance test: Child model without MappingStrategy, parent model with MappingStrategy.ANNOTATED"() {
		expect:
		InheritanceModelWithoutAnnotated inheritanceModel = modelProvider.get(InheritanceModelWithoutAnnotated.class, "/content/foo/jcr:content")
		Assert.assertNotNull(inheritanceModel)
		Assert.assertNull(inheritanceModel.getField1())
		Assert.assertNull(inheritanceModel.getField2())
	}
}
