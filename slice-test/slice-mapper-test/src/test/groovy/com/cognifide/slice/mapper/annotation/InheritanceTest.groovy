package com.cognifide.slice.mapper.annotation

import com.cognifide.slice.test.setup.BaseSetup
import org.junit.Assert

/**
 * @author Mariusz Kubiś Date: 15.04.15
 */
class InheritanceTest extends BaseSetup {

    def "Inheritance test"() {
        setup:
        pageBuilder.content {
            foo("cq:PageContent") {
                "jcr:content"("field1": "field1 value", "field2": "field2 value")
            }
        }

        expect:
        assertPageExists("/content/foo")
        InheritanceModel inheritanceModel = modelProvider.get(InheritanceModel.class, "/content/foo/jcr:content")
        Assert.assertNotNull(inheritanceModel)
        Assert.assertEquals(inheritanceModel.getField1(), "field1 value")
        Assert.assertEquals(inheritanceModel.getField2(), "field2 value")
    }
}
