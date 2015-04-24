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
                "jcr:content"("text": "Test", "style": "Style", "size": 5)
            }
        }
        expect:
        assertPageExists("/content/foo")
        JcrPropertyModel jcrPropertyModel = modelProvider.get(JcrPropertyModel.class, "/content/foo/jcr:content")
        Assert.assertNotNull(jcrPropertyModel)
        Assert.assertEquals(jcrPropertyModel.getText(), "Test")
        Assert.assertEquals(jcrPropertyModel.getSecondProperty(), "Style")
        Assert.assertEquals(jcrPropertyModel.getSize(), 5)
        Assert.assertEquals(jcrPropertyModel.getSizeFinal(), 0)
        Assert.assertEquals(jcrPropertyModel.getSizeStatic(), 0)
    }
}
