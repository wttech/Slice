package com.cognifide.slice.core.internal.injector

import com.citytechinc.aem.prosper.specs.ProsperSpec
import com.cognifide.slice.api.context.ContextScope
import com.cognifide.slice.api.injector.InjectorConfig
import com.cognifide.slice.api.injector.InjectorRunner
import com.cognifide.slice.core.internal.context.SliceContextScope
import com.cognifide.slice.core.internal.module.JcrModule
import com.cognifide.slice.core.internal.module.SliceModule
import com.cognifide.slice.core.internal.module.SliceResourceModule
import com.cognifide.slice.core.internal.module.SlingModule
import com.cognifide.slice.mapper.module.MapperModule
import com.cognifide.slice.test.module.TestModule
import com.google.inject.Module
import spock.lang.Shared

import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * Created by T530 on 2015-04-21.
 */
class InjectorsBaseTest  extends ProsperSpec{

    @Shared
    InjectorHierarchy injectorHierarchy

    @Shared
    InjectorsRepositoryService repositoryService

    def setup() {
        ContextScope contextScope = new SliceContextScope()
        List<Module> modules = new ArrayList<Module>()
        modules.add(new SliceModule(contextScope, null))
        modules.add(new SlingModule(contextScope))
        modules.add(new JcrModule())
        modules.add(new MapperModule())
        modules.add(new SliceResourceModule())
        modules.add(new TestModule())

        //Preparing configurations for injectors
        final InjectorRunner injectorRunner1 = new InjectorRunner(null,
                "slice-test",
                "slice-test-app.*",
                "com.cognifide.example")

        injectorRunner1.installModules(modules)
        InjectorConfig config = new InjectorConfig(injectorRunner1)

        final InjectorRunner injectorRunner2 = new InjectorRunner(null,
                "slice-test2",
                "slice-test-app2.*",
                "com.cognifide.example2")

        injectorRunner2.installModules(modules)
        injectorRunner2.setParentInjectorName("slice-test")
        InjectorConfig config2 = new InjectorConfig(injectorRunner2)

        final InjectorRunner injectorRunner3 = new InjectorRunner(null,
                "slice-test/subtest",
                "slice-test-app3.*",
                "com.cognifide.example3")

        injectorRunner3.installModules(modules)
        injectorRunner3.setParentInjectorName("slice-test")
        InjectorConfig config3 = new InjectorConfig(injectorRunner3)

        //creation of InjectorsRepositoryService with InjectorHierarchy
        repositoryService = new InjectorsRepositoryService()
        injectorHierarchy = new InjectorHierarchy()
        injectHierarchyIntoRepositoryService(repositoryService, injectorHierarchy)

        //creation and registration of injectors in the hierarchy
        Method bindConfigMethod = injectorHierarchy.getClass().getDeclaredMethod("bindConfig", InjectorConfig.class)
        bindConfigMethod.setAccessible(true)
        bindConfigMethod.invoke(injectorHierarchy, config)
        bindConfigMethod.invoke(injectorHierarchy, config2)
        bindConfigMethod.invoke(injectorHierarchy, config3)

    }

    private void injectHierarchyIntoRepositoryService(InjectorsRepositoryService repositoryService, InjectorHierarchy injectorHierarchy) {
        Field f = repositoryService.getClass().getDeclaredField("injectors")
        f.setAccessible(true)
        f.set(repositoryService, injectorHierarchy)
    }
}
