package com.cognifide.slice.mapper.annotation

import com.cognifide.slice.mapper.annotation.FollowModel
import com.cognifide.slice.mapper.annotation.JcrPropertyModel
import com.cognifide.slice.test.setup.BaseSetup
import org.junit.Assert

/**
 * @author Mariusz Kubiś
 * Date: 10.04.15
 */
class FollowTest extends BaseSetup {

    def "Follow test"() {
        setup:
        pageBuilder.content {
            foo("foo") {
                "jcr:content"("text": "Test", "style": "Style", "size": 5)
            }
        }

        pageBuilder.content {
            bar("bar") {
                "jcr:content"("jcrPropertyModel": "/content/foo/jcr:content")
            }
        }

        expect:
        assertPageExists("/content/foo")
        JcrPropertyModel jcrPropertyModel = modelProvider.get(JcrPropertyModel.class, "/content/foo/jcr:content")
        checkJcrPropertyModel(jcrPropertyModel)

        assertPageExists("/content/bar")
        FollowModel followModel = modelProvider.get(FollowModel.class, "/content/bar/jcr:content")
        checkJcrPropertyModel(followModel.getJcrPropertyModel())
    }

    private static void checkJcrPropertyModel(JcrPropertyModel model) {
        Assert.assertNotNull(model)
        Assert.assertEquals(model.getText(), "Test")
        Assert.assertEquals(model.getSecondProperty(), "Style")
        Assert.assertEquals(model.getSize(), 5)
    }
}
