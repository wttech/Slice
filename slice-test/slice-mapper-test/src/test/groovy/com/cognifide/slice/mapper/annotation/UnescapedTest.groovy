package com.cognifide.slice.mapper.annotation

import com.cognifide.slice.test.setup.BaseSetup
import org.junit.Assert

/**
 * @author Mariusz Kubiś Date: 15.04.15
 */
class UnescapedTest extends BaseSetup {

    def "Unescaped test"() {
        setup:
        pageBuilder.content {
            foo("cq:PageContent") {
                "jcr:content"("field1": "&\"\'<>", "field2": "&\"\'<>")
            }
        }

        expect:
        assertPageExists("/content/foo")
        UnescapedModel unescapedModel = modelProvider.get(UnescapedModel.class, "/content/foo/jcr:content")
        Assert.assertNotNull(unescapedModel)
        Assert.assertEquals(unescapedModel.getField1(), "&\"'<>")
        Assert.assertEquals(unescapedModel.getField2(), "&amp;&quot;&#39;&lt;&gt;")
    }
}
