package com.cognifide.slice.mapper.annotation

import com.cognifide.slice.test.setup.BaseSetup
import org.junit.Assert

/**
 * @author Mariusz Kubiś Date: 14.04.15
 */
class SliceReferenceTest extends BaseSetup {

    def "Slice Reference test"() {
        setup:
        pageBuilder.content {
            foo("cq:PageContent") {
                "jcr:content"("text": "Test1", "style": "Style", "size": 5)
            }
        }

        pageBuilder.content {
            bar("cq:PageContent") {
                "jcr:content"("text": "Test")
            }
        }

        expect:
        assertPageExists("/content/bar")
        assertPageExists("/content/foo")
        SliceReferenceModel sliceReferenceModel = modelProvider.get(SliceReferenceModel.class, "/content/bar/jcr:content")
        Assert.assertNotNull(sliceReferenceModel)
        Assert.assertEquals(sliceReferenceModel.getText(), "Test")
        JcrPropertyModel propertyModel = sliceReferenceModel.getPropertyModel()
        Assert.assertEquals(propertyModel.getText(), "Test1")
        Assert.assertEquals(propertyModel.getSecondProperty(), "Style")
        Assert.assertEquals(propertyModel.getSize(), 5)
    }
}
