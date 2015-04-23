package com.cognifide.slice.api.qualifier

import com.google.inject.Key
import junit.framework.Assert

/**
 * Created by Jaromir Celejewski on 2015-04-22.
 */
class SuffixInjectionTest extends InjectionTestBase {

    def "Get Suffix"() {
        setup: "Initialize context with a filled request"
        richRequestContext()
        and: "Get a suffix from injector"
        def suffix = injector.getInstance(Key.get(String.class, Suffix.class))

        expect: "Suffix is equal to 'suff' "
        Assert.assertEquals("suff", suffix)
    }

    def "Get Suffix - empty request"() {
        setup: "Initialize context with an empty request"
        emptyRequestContext()
        and: "Get a suffix from injector"
        def suffix = injector.getInstance(Key.get(String.class, Suffix.class))

        expect: "Suffix is equal to 'suff' "
        Assert.assertEquals("", suffix)
    }
}
