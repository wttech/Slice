package com.cognifide.slice.api.qualifier

import com.citytechinc.aem.prosper.specs.ProsperSpec
import com.cognifide.slice.api.context.ConstantContextProvider
import com.cognifide.slice.api.context.Context
import com.cognifide.slice.api.context.ContextFactory
import com.cognifide.slice.api.context.ContextScope
import com.cognifide.slice.api.injector.InjectorConfig
import com.cognifide.slice.api.injector.InjectorRunner
import com.cognifide.slice.api.provider.ModelProvider
import com.cognifide.slice.core.internal.context.SliceContextScope
import com.cognifide.slice.core.internal.injector.InjectorHierarchy
import com.cognifide.slice.core.internal.injector.InjectorsRepositoryService
import com.cognifide.slice.core.internal.module.JcrModule
import com.cognifide.slice.core.internal.module.SliceModule
import com.cognifide.slice.core.internal.module.SliceResourceModule
import com.cognifide.slice.core.internal.module.SlingModule
import com.cognifide.slice.mapper.module.MapperModule
import com.cognifide.slice.test.module.TestModule
import com.google.inject.Injector
import com.google.inject.Module
import junit.framework.Assert
import spock.lang.Shared

import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/22/15 1:58 PM
 */
class InjectorNameInjectionTest extends ProsperSpec {

    @Shared
    ModelProvider modelProvider

    @Shared
    SliceModule sliceModule

    @Shared
    Injector injector

    @Shared
    InjectorsRepositoryService repositoryService


    def setup() {
        ContextScope contextScope = new SliceContextScope()
        List<Module> modules = new ArrayList<Module>()
        modules.add(sliceModule = new SliceModule(contextScope, null))
        modules.add(new SlingModule(contextScope))
        modules.add(new JcrModule())
        modules.add(new MapperModule())
        modules.add(new SliceResourceModule())
        modules.add(new TestModule())

        //Preparing configurations for injectors
        final InjectorRunner injectorRunner = new InjectorRunner(null,
                "slice-test",
                "slice-test-app.*",
                "com.cognifide.slice")

        injectorRunner.installModules(modules)
        InjectorConfig config = new InjectorConfig(injectorRunner)

        //creation of InjectorsRepositoryService with InjectorHierarchy
        repositoryService = new InjectorsRepositoryService()
        def injectorHierarchy = new InjectorHierarchy()
        injectHierarchyIntoRepositoryService(repositoryService, injectorHierarchy)

        //creation and registration of injectors in the hierarchy
        Method bindConfigMethod = injectorHierarchy.getClass().getDeclaredMethod("bindConfig", InjectorConfig.class)
        bindConfigMethod.setAccessible(true)
        bindConfigMethod.invoke(injectorHierarchy, config)

        injector = injectorHierarchy.getInjectorByName("slice-test")
        ContextFactory factory = injector.getInstance(ContextFactory.class)
        Context context = factory.getResourceResolverContext(resourceResolver)
        contextScope.setContextProvider(new ConstantContextProvider(context))
        modelProvider = injector.getInstance(ModelProvider.class)

    }

    /**
     * Since RepositoryService is a OSGi service and it's being injected by Peaberry, test invokes getInjectorName method
     * directly.
     */
    def "Get Injector Name"() {
        setup: "Get Injector name from SliceModule"
        def injectorName = sliceModule.getInjectorName(repositoryService, injector)

        expect: "Injector name is not null"
        Assert.assertNotNull(injectorName)

        and: "Injector name is equal to 'injector-name'"
        Assert.assertEquals("slice-test", injectorName)
    }

    private void injectHierarchyIntoRepositoryService(InjectorsRepositoryService repositoryService, InjectorHierarchy injectorHierarchy) {
        Field f = repositoryService.getClass().getDeclaredField("injectors")
        f.setAccessible(true)
        f.set(repositoryService, injectorHierarchy)
    }

}
