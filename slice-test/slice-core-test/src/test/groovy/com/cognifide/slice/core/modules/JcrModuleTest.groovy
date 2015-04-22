package com.cognifide.slice.core.modules

import com.cognifide.slice.core.internal.module.JcrModule
import com.cognifide.slice.test.setup.BaseSetup
import junit.framework.Assert

import javax.jcr.query.QueryManager

/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/22/15 8:48 AM
 */
class JcrModuleTest extends BaseSetup {

    def "Get Query Manager test"() {
        setup: "Get JcrModule from injector"
        def module = injector.getInstance(JcrModule.class)
        and: "Get Query Manager by a null Resource Resolver"
        def queryManager = module.getQueryManager(resourceResolver)

        expect: "Module is not null"
        Assert.assertNotNull(module)

        and: "Query Manager is not null"
        Assert.assertNotNull(queryManager);

        and:
        Assert.assertTrue(queryManager instanceof QueryManager)
    }

    def "Query Manager Manager by null Resource Resolver"() {
        setup: "Get JcrModule from injector"
        def module = injector.getInstance(JcrModule.class)

        when: "Get Query Manager by a null Resource Resolver"
        module.getQueryManager(null)
        then: "Throw NullPointerException"
        thrown(NullPointerException)
    }


}
