package com.cognifide.slice.core.provider

import com.cognifide.slice.test.module.SimpleCacheableModel
import com.cognifide.slice.test.module.SimpleModel
import com.cognifide.slice.test.setup.BaseSetup
import org.junit.Assert

/**
 * Created by T530 on 2015-04-21.
 */
class SliceModelProviderCacheableTest extends BaseSetup {

    def setupSpec() {
        setup: "Create simple, initial content - node 'foo' with property 'prop1' set to 'prop1Value'"
        pageBuilder.content {
            foo("foo") {
                "jcr:content"("prop1": "prop1Value")
            }
        }
    }

    def "Get non cacheable model by class and path"() {
        def path = "/content/foo"

        setup: "Creating model for path: " << path
        SimpleModel modelA = modelProvider.get(SimpleModel.class, path);
        SimpleModel modelB = modelProvider.get(SimpleModel.class, path);

        expect: "Model should not be null"
        Assert.assertNotNull(modelA)
        Assert.assertNotNull(modelB)

        and: "Model provider should deliver new model instance on each request"
        Assert.assertTrue(modelA!=modelB)
    }

    def "Get cacheable model by class and path"() {
        def path = "/content/foo"

        setup: "Creating model for path: " << path

        SimpleCacheableModel modelA = modelProvider.get(SimpleCacheableModel.class, path);
        SimpleCacheableModel modelB = modelProvider.get(SimpleCacheableModel.class, path);

        expect: "Model should not be null"
        Assert.assertNotNull(modelA)
        Assert.assertNotNull(modelB)

        and: "Model provider should deliver the same model instance on each request"
        Assert.assertTrue(modelA==modelB)
    }
}
