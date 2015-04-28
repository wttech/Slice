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
        setup: "Initialize context"
        pageBuilder.content {
            testPath("cq:PageContent") {
                "jcr:content"("text": "Test", "style": "Style")
            }
        }
        when:
        Resource resource = getResource(path)
        CurrentResourcePathModel currentResourcePathModel = modelProvider.get(CurrentResourcePathModel.class, resource)

        then: "Resource exists and model has been properly initialized"
        Assert.assertNotNull(resource)
        Assert.assertNotNull(currentResourcePathModel)
        Assert.assertEquals(currentResourcePathModel.getCurrentResourcePath(), path)
    }

    def "Get current resource path model by path"() {
        given: "Example content"
        when:
        CurrentResourcePathModel currentResourcePathModel = modelProvider.get(CurrentResourcePathModel.class, path)
        then: "Model has been property initialized"
        Assert.assertNotNull(currentResourcePathModel)
        Assert.assertEquals(currentResourcePathModel.getCurrentResourcePath(), path)
    }
}
