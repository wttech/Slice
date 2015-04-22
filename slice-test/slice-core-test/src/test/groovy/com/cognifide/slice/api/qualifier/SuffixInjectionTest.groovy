package com.cognifide.slice.api.qualifier

import junit.framework.Assert

/**
 * Created by Jaromir Celejewski on 2015-04-22.
 */
class SuffixInjectionTest extends InjectionTestBase{

    def "Get Suffix"() {
        setup: "Init model"
        richRequestContext()
        def SuffixInjectionModel model = modelProvider.get(SuffixInjectionModel.class, '/content/foo')
        def suffix = model.getSuffix()

        expect: "There is a suffix equal to "
        Assert.assertEquals("suff", suffix)
    }

    def "Get Empty Suffix"() {
        setup: "Init model"
        emptyRequestContext()
        def SuffixInjectionModel model = modelProvider.get(SuffixInjectionModel.class, '/content/foo')
        def suffix = model.getSuffix()

        expect: "Suffix is an empty string"
        Assert.assertEquals(suffix, "")
    }

}
