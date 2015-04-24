package com.cognifide.slice.core.internal.adapter

import com.cognifide.slice.test.module.SimpleModel
import junit.framework.Assert


/**
 * Created by Jaromir Celejewski on 2015-04-23.
 */
class SliceAdapterFactoryTest extends AdapterBaseSetup {

    def setupSpec() {
        setup: "Create simple, initial content - node 'foo' with property 'prop1' set to 'prop1Value'"
        pageBuilder.content {
            foo("foo") {
                "jcr:content"("prop1": "prop1Value")
            }
        }
    }

    def "resource is adaptable to multiple types without RequestContextProvider"() {
        setup:
        def path = "/content/foo/jcr:content"
        def resource = resourceResolver.getResource(path)
        def SimpleModel model = resource.adaptTo(SimpleModel.class)

        expect:
        Assert.assertNotNull(model)

        and:
        Assert.assertEquals("prop1Value", model.getProp1())
    }

    def "resource is adaptable to multiple types with RequestContextProvider"() {
        setup:
        injectPrivateField(injectorRepositoryAdapterFactory, createRequestContextProvider(), "requestContextProvider")

        def path = "/content/foo/jcr:content"
        def resource = resourceResolver.getResource(path)
        def SimpleModel model = resource.adaptTo(SimpleModel.class)

        expect:
        Assert.assertNotNull(model)

        and:
        Assert.assertEquals("prop1Value", model.getProp1())
    }



}