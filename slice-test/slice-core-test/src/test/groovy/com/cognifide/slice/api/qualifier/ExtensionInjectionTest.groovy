package com.cognifide.slice.api.qualifier

import junit.framework.Assert

/**
 * Created by Jaromir Celejewski on 2015-04-22.
 */
class ExtensionInjectionTest extends InjectionTestBase{

    def "Get Extension"() {
        setup: "Init model"
        richRequestContext()
        def ExtensionInjectionModel model = modelProvider.get(ExtensionInjectionModel.class, '/content/foo')
        def extension = model.getExtension()

        expect: "There is a html extension"
        Assert.assertEquals("html", extension)
    }

    def "Get Empty Extension"() {
        setup: "Init model"
        emptyRequestContext()
        def ExtensionInjectionModel model = modelProvider.get(ExtensionInjectionModel.class, '/content/foo')
        def extension = model.getExtension()

        expect: "Extension is an empty string"
        Assert.assertEquals("", extension)
    }

}
