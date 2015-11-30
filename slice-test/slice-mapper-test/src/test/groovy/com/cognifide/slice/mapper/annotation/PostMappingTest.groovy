package com.cognifide.slice.mapper.annotation

import com.cognifide.slice.test.setup.BaseSetup
import org.junit.Assert

/**
 * @author Krzysztof Watral
 */
class PostMappingTest extends BaseSetup {

    def "PreMapping Annotation Test"() {
        setup: "Creating initial content"
        pageBuilder.content {
            test("cq:PageContent") {
                "jcr:content"("field": "some value")
            }
        }

        when: "Get a model instance by path"
        PostMappingModel postMappingModel = modelProvider.get(PostMappingModel.class, "/content/test/jcr:content")

        then: "Model has been property initialized"
        Assert.assertNotNull(postMappingModel)
        Assert.assertEquals("some value_TEST", postMappingModel.getField())
    }
}
