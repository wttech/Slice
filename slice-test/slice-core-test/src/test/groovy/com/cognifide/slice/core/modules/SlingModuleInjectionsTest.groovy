package com.cognifide.slice.core.modules

import com.citytechinc.aem.prosper.specs.ProsperSpec
import com.cognifide.slice.api.context.ConstantContextProvider
import com.cognifide.slice.api.context.Context
import com.cognifide.slice.api.context.ContextFactory
import com.cognifide.slice.api.context.ContextScope
import com.cognifide.slice.api.provider.ModelProvider
import com.cognifide.slice.core.internal.context.SliceContextScope
import com.cognifide.slice.core.internal.module.JcrModule
import com.cognifide.slice.core.internal.module.SliceModule
import com.cognifide.slice.core.internal.module.SliceResourceModule
import com.cognifide.slice.core.internal.module.SlingModule
import com.cognifide.slice.mapper.module.MapperModule
import com.cognifide.slice.test.module.TestModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import junit.framework.Assert
import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.api.SlingHttpServletResponse
import org.apache.sling.api.resource.ResourceResolver
import spock.lang.Shared

/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/22/15 10:55 AM
 */
class SlingModuleInjectionsTest extends ProsperSpec {

    @Shared
    SlingHttpServletRequest request

    @Shared
    SlingHttpServletResponse response

    @Shared
    ModelProvider modelProvider

    @Shared
    SlingModule slingModule

    @Shared
    Injector injector

    def setupSpec() {
        ContextScope contextScope = new SliceContextScope()
        List<Module> modules = new ArrayList<Module>()
        modules.add(new SliceModule(contextScope, null))
        modules.add(slingModule = new SlingModule(contextScope))
        modules.add(new JcrModule())
        modules.add(new MapperModule())
        modules.add(new SliceResourceModule())
        modules.add(new TestModule())

        injector = Guice.createInjector(modules)

        request = requestBuilder.build {
            parameters = [path: "/content/prosper"]
            selectors = ["one", "two"]
            contentType = "application/json"
            extension = "html"
            suffix = "suff"
        }

        response = responseBuilder.build()

        ContextFactory factory = injector.getInstance(ContextFactory.class)

        Context context = factory.getServletRequestContext("injector-name", request, response)
        contextScope.setContextProvider(new ConstantContextProvider(context))

        modelProvider = injector.getInstance(ModelProvider.class)
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

    def "Get Suffix by a null Request Path Info"() {
        when: "Get Suffix by a null Request Path Info"
        slingModule.getSuffix(null)

        then: "Thrown Null Pointer Exception"
        thrown(NullPointerException)
    }



}
