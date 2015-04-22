package com.cognifide.slice.core.modules

import com.cognifide.slice.test.setup.BaseSetup
import junit.framework.Assert
import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.api.SlingHttpServletResponse
import spock.lang.Shared

/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/22/15 9:28 AM
 */
class SlingModuleTest extends BaseSetup {

    @Shared
    SlingHttpServletRequest request

    @Shared
    SlingHttpServletResponse response

    def setupSpec() {
        nodeBuilder.content {
            prosper()
        }

        request = requestBuilder.build {
            parameters = [path: "/content/prosper"]
            selectors = ["one", "two"]
            contentType = "application/json"
        }

        response = responseBuilder.build()
    }

    def "Get Sling Http Servlet Request"() {
        setup: "Create servlet request"
        def slingHttpServletRequest = slingModule.getSlingHttpServletRequest(request)

        expect: "Request is not null"
        Assert.assertNotNull(slingHttpServletRequest)
    }

    def "Get Sling Http Servlet Request by a wrong class request"() {
        when: "Create servlet request"
        slingModule.getSlingHttpServletRequest(null)

        then: "Throw Illegal State Exception - request has an invalid class"
        thrown(IllegalStateException)
    }

    def "Get Sling Http Servlet Response by a wrong class request"() {
        when: "Create servlet request"
        slingModule.getSlingHttpServletResponse(null)

        then: "Throw Illegal State Exception - response has an invalid class"
        thrown(IllegalStateException)
    }

}
