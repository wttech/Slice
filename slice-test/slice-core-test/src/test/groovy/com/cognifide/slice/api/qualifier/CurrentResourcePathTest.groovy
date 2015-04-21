package com.cognifide.slice.api.qualifier

import com.cognifide.slice.test.setup.BaseSetup
import org.apache.sling.api.resource.Resource
import org.junit.Assert

/**
 * @author Mariusz Kubiś
 * Date: 10.04.15
 */
class CurrentResourcePathTest extends BaseSetup {

    private String path = "/content/testPath/jcr:content"

    def "Get current resource path model by resource"() {
        setup:
        pageBuilder.content {
            testPath("cq:PageContent") {
                "jcr:content"("text": "Test", "style": "Style")
            }
        }
        expect:
        Resource resource = getResource(path);
        Assert.assertNotNull(resource)
        CurrentResourcePathModel currentResourcePathModel = modelProvider.get(CurrentResourcePathModel.class, resource)
        Assert.assertNotNull(currentResourcePathModel)
        Assert.assertEquals(currentResourcePathModel.getCurrentResourcePath(), path)
    }

    def "Get current resource path model by path"() {
        expect:
        CurrentResourcePathModel currentResourcePathModel = modelProvider.get(CurrentResourcePathModel.class, path)
        Assert.assertNotNull(currentResourcePathModel)
        Assert.assertEquals(currentResourcePathModel.getCurrentResourcePath(), path)
    }
}
