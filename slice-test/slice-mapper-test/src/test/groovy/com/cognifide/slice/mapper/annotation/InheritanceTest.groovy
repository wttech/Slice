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
                "jcr:content"("field1": "field1 value", "field2": "field2 value")
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
    }

    def "Inheritance test: Child model with MappingStrategy.ANNOTATED, parent model with MappingStrategy.ALL"() {
        expect:
        InheritanceModelAnnotatedAll inheritanceModel = modelProvider.get(InheritanceModelAnnotatedAll.class, "/content/foo/jcr:content")
        Assert.assertNotNull(inheritanceModel)
        Assert.assertEquals(inheritanceModel.getField1(), "field1 value")
        Assert.assertEquals(inheritanceModel.getField2(), "field2 value")
    }

    def "Inheritance test: Child model with MappingStrategy.ANNOTATED, parent model without MappingStrategy"() {
        expect:
        InheritanceModelAnnotatedWithout inheritanceModel = modelProvider.get(InheritanceModelAnnotatedWithout.class, "/content/foo/jcr:content")
        Assert.assertNotNull(inheritanceModel)
        Assert.assertNull(inheritanceModel.getField1())
        Assert.assertEquals(inheritanceModel.getField2(), "field2 value")
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
