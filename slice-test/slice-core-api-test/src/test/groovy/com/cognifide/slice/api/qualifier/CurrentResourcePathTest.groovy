package com.cognifide.slice.api.qualifier

import com.cognifide.slice.test.setup.BaseSetup
import org.junit.Assert

/**
 * @author Mariusz Kubiś
 * Date: 10.04.15
 */
class CurrentResourcePathTest extends BaseSetup {

    private String path = "/content/testPath/jcr:content"

    def "Current resource path test"() {
        expect:
        CurrentResourcePathModel currentResourcePathModel = modelProvider.get(CurrentResourcePathModel.class, path)
        Assert.assertNotNull(currentResourcePathModel)
        Assert.assertEquals(currentResourcePathModel.getCurrentResourcePath(), path)
    }
}
