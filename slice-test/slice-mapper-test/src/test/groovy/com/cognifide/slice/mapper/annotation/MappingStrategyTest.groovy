package com.cognifide.slice.mapper.annotation

import com.cognifide.slice.test.setup.BaseSetup
import org.junit.Assert

/**
 * @author Mariusz Kubiś Date: 15.04.15
 */
class MappingStrategyTest extends BaseSetup {

    def "Mapping Strategy Test"() {
        setup:
        pageBuilder.content {
            foo("cq:PageContent") {
                "jcr:content"("field1": "field1 value", "field2": "field2 value")
            }
        }
        expect:
        assertPageExists("/content/foo")
    }

    def "Mapping Strategy All Test"() {
        expect:
        MappingStrategyAllModel mappingStrategyAllModel = modelProvider.get(MappingStrategyAllModel.class, "/content/foo/jcr:content")
        Assert.assertNotNull(mappingStrategyAllModel)
        Assert.assertEquals(mappingStrategyAllModel.getField1(), "field1 value")
        Assert.assertEquals(mappingStrategyAllModel.getField2(), "field2 value")
    }

    def "Mapping Strategy Annotated Test"() {
        expect:
        MappingStrategyAnnotatedModel mappingStrategyAnnotatedModel = modelProvider.get(MappingStrategyAnnotatedModel.class, "/content/foo/jcr:content")
        Assert.assertNotNull(mappingStrategyAnnotatedModel)
        Assert.assertEquals(mappingStrategyAnnotatedModel.getField1(), "field1 value")
        Assert.assertNull(mappingStrategyAnnotatedModel.getField2())
    }
}
