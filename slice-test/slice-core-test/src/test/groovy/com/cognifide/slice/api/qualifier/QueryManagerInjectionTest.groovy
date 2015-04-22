package com.cognifide.slice.api.qualifier

import junit.framework.Assert

import javax.jcr.query.QueryManager

/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/22/15 3:39 PM
 */
class QueryManagerInjectionTest extends InjectionTestBase {

    def "Get Query Manager"() {
        setup: "Init model"
        richRequestContext()
        def model = modelProvider.get(QueryManagerInjectionModel.class, "/content/foo")
        def queryManager = model.getQueryManager()

        expect: "Query Manager is not null"
        Assert.assertNotNull(queryManager)

        and: "Query Manager is an instance of a QueryManager"
        Assert.assertTrue(queryManager instanceof QueryManager)
    }

    def "Get Query Manager with an empty request"() {
        setup: "Init model"
        emptyRequestContext()
        def model = modelProvider.get(QueryManagerInjectionModel.class, "/content/foo")
        def queryManager = model.getQueryManager()

        expect: "Query Manager is not null"
        Assert.assertNotNull(queryManager)

        and: "Query Manager is an instance of a QueryManager"
        Assert.assertTrue(queryManager instanceof QueryManager)
    }

}
