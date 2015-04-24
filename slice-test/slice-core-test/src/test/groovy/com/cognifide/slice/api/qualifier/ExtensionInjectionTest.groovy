package com.cognifide.slice.api.qualifier

import com.google.inject.Key
import junit.framework.Assert

/**
 * Created by Jaromir Celejewski on 2015-04-22.
 */
class ExtensionInjectionTest extends InjectionTestBase {

    def "Get Extension"() {
        setup: "Initialize context with a filled request"
        richRequestContext()
        and: "Get extension from injector"
        def extension = injector.getInstance(Key.get(String.class, Extension.class))

        expect: "There is a html extension"
        Assert.assertEquals("html", extension)
    }

    def "Get Empty Extension"() {
        setup: "Initialize context with an empty request (no extension)"
        emptyRequestContext()
        and: "Get extension from injector"
        def extension = injector.getInstance(Key.get(String.class, Extension.class))

        expect: "Extension is an empty string"
        Assert.assertEquals("", extension)
    }

}
