package com.cognifide.slice.mapper.annotation

import com.cognifide.slice.test.setup.BaseSetup
import org.junit.Assert

/**
 * @author Mariusz Kubiś Date: 14.04.15
 */
class IgnorePropertyTest extends BaseSetup {

    def "Ignore property test"() {
        setup:
        pageBuilder.content {
            foo("foo") {
                "jcr:content"("text": "Test", "style": "Style")
            }
        }
        def contentNode = session.getNode("/content/foo/jcr:content")
        IgnorePropertyModel ignorePropertyModel = modelProvider.get(IgnorePropertyModel.class, "/content/foo/jcr:content")

        expect:
        Assert.assertEquals(contentNode.get("style"), "Style")
        Assert.assertNotNull(ignorePropertyModel)
        Assert.assertEquals(ignorePropertyModel.getText(), "Test")
        Assert.assertNull(ignorePropertyModel.getStyle())

    }
}
