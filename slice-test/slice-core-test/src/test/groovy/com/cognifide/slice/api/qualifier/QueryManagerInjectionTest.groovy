package com.cognifide.slice.api.qualifier

import com.google.inject.Key
import junit.framework.Assert

import javax.jcr.query.QueryManager

/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/22/15 3:39 PM
 */
class QueryManagerInjectionTest extends InjectionTestBase {

    def "Get Query Manager"() {
        setup: "Initialize context with a filled request"
        richRequestContext()
        and: "Get Query Manager from injector"
        def queryManager = injector.getInstance(Key.get(QueryManager.class))

        expect: "Query Manager is not null"
        Assert.assertNotNull(queryManager)

        and: "Query Manager is an instance of a QueryManager"
        Assert.assertTrue(queryManager instanceof QueryManager)
    }

    def "Get Query Manager - empty request"() {
        setup: "Initialize context with an empty request"
        emptyRequestContext()
        and: "Get Query Manager from injector"
        def queryManager = injector.getInstance(Key.get(QueryManager.class))

        expect: "Query Manager is not null"
        Assert.assertNotNull(queryManager)

        and: "Query Manager is an instance of a QueryManager"
        Assert.assertTrue(queryManager instanceof QueryManager)
    }

}