package com.cognifide.slice.mapper.annotation

import com.cognifide.slice.test.setup.BaseSetup
import org.junit.Assert

/**
 * @author Mariusz Kubiś Date: 15.04.15
 */
class UnescapedTest extends BaseSetup {

    def setupSpec() {
        setup: "Create a content under '/content/foo' with two properties"
        pageBuilder.content {
            foo("cq:PageContent") {
                "jcr:content"("field1": "&\"\'<>", "field2": "&\"\'<>")
            }
        }
    }

    def "Unescaped test"() {
        def path = "/content/foo"

        setup: "Get an UnescapedModel, which contains one unescaped and one escaped property"
        UnescapedModel unescapedModel = modelProvider.get(UnescapedModel.class, path + "/jcr:content")

        expect: "Content was successfully created"
        assertPageExists(path)

        and: "Model is not null"
        Assert.assertNotNull(unescapedModel)

        and: "Field1 was not escaped"
        Assert.assertEquals(unescapedModel.getField1(), "&\"'<>")

        and: "Field2 was escaped"
        Assert.assertEquals(unescapedModel.getField2(), "&amp;&quot;&#39;&lt;&gt;")
    }
}
