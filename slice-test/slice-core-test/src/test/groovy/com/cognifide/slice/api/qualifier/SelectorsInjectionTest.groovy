package com.cognifide.slice.api.qualifier

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
import com.cognifide.slice.core.modules.SlingModuleInjectionsModel
import com.cognifide.slice.mapper.module.MapperModule
import com.cognifide.slice.test.module.TestModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import junit.framework.Assert
import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.api.SlingHttpServletResponse
import spock.lang.Shared

/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/22/15 11:30 AM
 */
class SelectorsInjectionTest extends ProsperSpec {

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

    @Shared
    ContextScope contextScope

    def setupSpec() {
        contextScope = new SliceContextScope()
        List<Module> modules = new ArrayList<Module>()
        modules.add(new SliceModule(contextScope, null))
        modules.add(slingModule = new SlingModule(contextScope))
        modules.add(new JcrModule())
        modules.add(new MapperModule())
        modules.add(new SliceResourceModule())
        modules.add(new TestModule())

        injector = Guice.createInjector(modules)
    }

    def notNullRequestContext() {
        request = requestBuilder.build {
            parameters = [path: "/content/prosper"]
            selectors = ["one", "two"]
            contentType = "application/json"
            extension = "html"
            suffix = "suff"
        }

        response = responseBuilder.build()

        initContext(request, response)
    }

    def emptyRequestContext() {
        request = requestBuilder.build()
        response = responseBuilder.build()

        initContext(request, response)
    }

    def initContext(request, response) {
        ContextFactory factory = injector.getInstance(ContextFactory.class)

        Context context = factory.getServletRequestContext("injector-name", request, response)
        contextScope.setContextProvider(new ConstantContextProvider(context))

        modelProvider = injector.getInstance(ModelProvider.class)
    }

    def "Get Selectors"() {
        setup: "Init model"
        notNullRequestContext()
        def SlingModuleInjectionsModel model = modelProvider.get(SlingModuleInjectionsModel.class, '/content/foo')
        def selectors = model.getSelectors()

        expect: "Selectors are not null"
        Assert.assertNotNull(selectors)

        and: "There are two selectors"
        Assert.assertEquals(2, selectors.size())

        and: "First selector is equal to 'one'"
        Assert.assertEquals("one", selectors[0])

        and: "Second selector is equal to 'two'"
        Assert.assertEquals("two", selectors[1])
    }

    def "Get Selectors as List"() {
        setup: "Init model"
        notNullRequestContext()
        def SlingModuleInjectionsModel model = modelProvider.get(SlingModuleInjectionsModel.class, '/content/foo')
        def selectors = model.getSelectorsAsList()

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

    def "Get Selectors String"() {
        setup: "Init model"
        notNullRequestContext()
        def SlingModuleInjectionsModel model = modelProvider.get(SlingModuleInjectionsModel.class, '/content/foo')
        def selectors = model.getSelectorsAsString()

        expect: "Selectors string is not null"
        Assert.assertNotNull(selectors)

        and: "Selectors string is equal to 'one.two'"
        Assert.assertEquals("one.two", selectors)
    }

}
