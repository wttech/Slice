package com.cognifide.slice.mapper.impl.processor

import com.cognifide.slice.mapper.annotation.BooleanInjectionModel
import com.cognifide.slice.test.setup.BaseSetup
import junit.framework.Assert

/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/23/15 1:15 PM
 */
class BooleanInjectionTest extends BaseSetup {

    def "Get a model with a Boolean mapped from String (true)"() {
        setup: "Create a content with boolean value (String)"
        def path = "/content/foo/jcr:content"
        pageBuilder.content {
            foo("foo") {
                "jcr:content"("boolProp": "true")
            }
        }
        and: "Get a model"
        def model = modelProvider.get(BooleanInjectionModel.class, path)

        expect: "Model is not null"
        Assert.assertNotNull(model)

        and: "Boolean property was correctly mapped from String"
        Assert.assertEquals(Boolean.TRUE, model.getBoolProp())
    }

    def "Get a model with a Boolean mapped from String (false)"() {
        setup: "Create a content with boolean value (String)"
        def path = "/content/foo/jcr:content"
        pageBuilder.content {
            foo("foo") {
                "jcr:content"("boolProp": "false")
            }
        }
        and: "Get a model"
        def model = modelProvider.get(BooleanInjectionModel.class, path)

        expect: "Model is not null"
        Assert.assertNotNull(model)

        and: "Boolean property was correctly mapped from String"
        Assert.assertEquals(Boolean.FALSE, model.getBoolProp())
    }

    def "Get a model with a Boolean mapped from Boolean (true)"() {
        setup: "Create a content with boolean values"
        def path = "/content/foo/jcr:content"
        pageBuilder.content {
            foo("foo") {
                "jcr:content"("boolProp": true)
            }
        }
        and: "Get a model"
        def model = modelProvider.get(BooleanInjectionModel.class, path)

        expect: "Model is not null"
        Assert.assertNotNull(model)

        and: "Boolean property was correctly mapped from Boolean"
        Assert.assertEquals(Boolean.TRUE, model.getBoolProp())
    }

    def "Get model with a Boolean mapped from null"() {
        setup: "Create a content with boolean values"
        def path = "/content/foo/jcr:content"
        pageBuilder.content {
            foo("foo") {
                "jcr:content"("boolProp": null)
            }
        }
        and: "Get a model"
        def model = modelProvider.get(BooleanInjectionModel.class, path)

        expect: "Model is not null"
        Assert.assertNotNull(model)

        and: "Boolean property was correctly mapped from Null"
        Assert.assertNull(model.getBoolProp())
    }


    def "Get model with a boolean mapped from Boolean (true)"() {
        setup: "Create a content with boolean values"
        def path = "/content/foo/jcr:content"
        pageBuilder.content {
            foo("foo") {
                "jcr:content"("primitiveBoolProp": true)
            }
        }
        and: "Get a model"
        def model = modelProvider.get(BooleanInjectionModel.class, path)

        expect: "Model is not null"
        Assert.assertNotNull(model)

        and: "Boolean property was correctly mapped from Boolean"
        Assert.assertTrue(model.isPrimitiveBoolProp())
    }

    def "Get model with a boolean mapped from Boolean (false)"() {
        setup: "Create a content with boolean values"
        def path = "/content/foo/jcr:content"
        pageBuilder.content {
            foo("foo") {
                "jcr:content"("primitiveBoolProp": false)
            }
        }
        and: "Get a model"
        def model = modelProvider.get(BooleanInjectionModel.class, path)

        expect: "Model is not null"
        Assert.assertNotNull(model)

        and: "Boolean property was correctly mapped from Boolean"
        Assert.assertFalse(model.isPrimitiveBoolProp())
    }

    def "Get model with boolean mapped from Boolean (null)"() {
        setup: "Create a content with boolean values"
        def path = "/content/foo/jcr:content"
        pageBuilder.content {
            foo("foo") {
                "jcr:content"("primitiveBoolProp": null)
            }
        }
        and: "Get a model"
        def model = modelProvider.get(BooleanInjectionModel.class, path)

        expect: "Model is not null"
        Assert.assertNotNull(model)

        and: "Boolean property was correctly mapped from Boolean"
        Assert.assertFalse(model.isPrimitiveBoolProp())
    }

}