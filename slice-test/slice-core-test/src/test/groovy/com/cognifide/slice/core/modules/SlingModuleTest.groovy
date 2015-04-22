package com.cognifide.slice.core.modules

import com.cognifide.slice.test.setup.BaseSetup
import junit.framework.Assert
import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.api.SlingHttpServletResponse
import org.apache.sling.api.resource.ResourceResolver
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
            extension = "html"
            suffix = "suff"
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

    def "Get Resource Resolver"() {
        setup: "Get Resource Resolver"
        def resourceResolver = slingModule.getResourceResolver(request)

        expect: "Resource Resolver is not null"
        Assert.assertNotNull(resourceResolver)

        and: "Resource Resolver has a correct class"
        Assert.assertTrue(resourceResolver instanceof ResourceResolver)
    }

    def "Get Resource Resolver by a null request"() {
        when: "Get Resource Resolver"
        slingModule.getResourceResolver(null)

        then: "Throw Null Pointer Exception"
        thrown(NullPointerException)
    }

    def "Get Request Path Info"() {
        setup: "Get Request Path Info"
        def requestPathInfo = slingModule.getRequestPathInfo(request)

        expect: "Request Path Info is not null"
        Assert.assertNotNull(requestPathInfo)
    }

    def "Get Request Path Info by a null request"() {
        when: "Get Request Path Info by a null request"
        slingModule.getRequestPathInfo(null)

        then: "Throw Null Pointer Exception"
        thrown(NullPointerException)
    }

    def "Get Extension"() {
        setup: "Get extension"
        def extension = slingModule.getExtension(request.getRequestPathInfo())

        expect: "Extension is not null"
        Assert.assertNotNull(extension)

        and: "Extension is equal to html"
        Assert.assertEquals("html", extension)
    }

    def "Get Extension by a null Request Path Info"() {
        when: "Get extension from null Request Path Info"
        slingModule.getExtension(null)

        then: "Throw Null Pointer Exception"
        thrown(NullPointerException)
    }

    def "Get Suffix"() {
        setup: "Get Suffix"
        def suffix = slingModule.getSuffix(request.getRequestPathInfo())

        expect: "Suffix is not null"
        Assert.assertNotNull(suffix)

        and: "Suffix is equal to 'suff'"
        Assert.assertEquals("suff", suffix)
    }

    def "Get Suffix by a null Request Path Info"() {
        when: "Get Suffix by a null Request Path Info"
        slingModule.getSuffix(null)

        then: "Thrown Null Pointer Exception"
        thrown(NullPointerException)
    }

    def "Get Selectors"() {
        setup: "Get Selectors"
        def selectors = slingModule.getSelectors(request.getRequestPathInfo())

        expect: "Selectors are not null"
        Assert.assertNotNull(selectors)

        and: "There are two selectors"
        Assert.assertEquals(2, selectors.size())

        and: "First selector is equal to 'one'"
        Assert.assertEquals("one", selectors[0])

        and: "Second selector is equal to 'two'"
        Assert.assertEquals("two", selectors[1])
    }

    def "Get Selectors by a null Request Path Info"() {
        when: "Get Selectors by a null Request Path Info"
        slingModule.getSelectors(null)

        then: "Null Pointer Exception is thrown"
        thrown(NullPointerException)
    }

    def "Get Selectors as List"() {
        setup: "Get Selectors as List"
        def selectors = slingModule.getSelectorsAsList(request.getRequestPathInfo())

        expect: "Selectors are not null"
        Assert.assertNotNull(selectors)

        and: "Selectors are returned as a List"
        Assert.assertTrue(selectors instanceof List)

        and: "There are two selectors"
        Assert.assertEquals(2, selectors.size())

        and: "First selector is equal to 'one'"
        Assert.assertEquals("one", selectors[0])

        and: "Second selector is equal to 'two'"
        Assert.assertEquals("two", selectors[1])
    }

    def "Get Selectors as List by a null Request Path Info"() {
        when: "Get Selectors by a null Request Path Info"
        slingModule.getSelectorsAsList(null)

        then: "Selectors are not null"
        thrown(NullPointerException)
    }

    def "Get Selectors String"() {
        setup: "Get Selectors String"
        def selectors = slingModule.getSelectorsString(request.getRequestPathInfo())

        expect: "Selectors string is not null"
        Assert.assertNotNull(selectors)

        and: "Selectors string is equal to 'one.two'"
        Assert.assertEquals("one.two", selectors)
    }

}
