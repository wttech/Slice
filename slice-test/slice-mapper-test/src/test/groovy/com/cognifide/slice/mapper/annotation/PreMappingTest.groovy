package com.cognifide.slice.mapper.annotation

import com.cognifide.slice.test.setup.BaseSetup
import org.junit.Assert

/**
 * @author Krzysztof Watral
 */
class PreMappingTest extends BaseSetup {

    def "PreMapping Annotation Test"() {
        setup: "Creating initial content"
        pageBuilder.content {
            test("cq:PageContent") {
                "jcr:content"("field": "some value")
            }
        }

        when: "Get a model instance by path"
        PreMappingModel preMappingModel = modelProvider.get(PreMappingModel.class, "/content/test/jcr:content")

        then: "Model has been property initialized"
        Assert.assertNotNull(preMappingModel)
        Assert.assertFalse(preMappingModel.wasModelSet());
        Assert.assertEquals("SOME VALUE", preMappingModel.getField())
    }
}
