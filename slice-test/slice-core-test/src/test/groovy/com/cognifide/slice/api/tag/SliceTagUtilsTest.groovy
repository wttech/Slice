package com.cognifide.slice.api.tag

import com.citytechinc.aem.prosper.specs.ProsperSpec
import com.cognifide.slice.api.context.ConstantContextProvider
import com.cognifide.slice.api.context.Context
import com.cognifide.slice.api.context.ContextFactory
import com.cognifide.slice.api.context.ContextScope
import com.cognifide.slice.api.injector.InjectorConfig
import com.cognifide.slice.api.injector.InjectorRunner
import com.cognifide.slice.api.injector.InjectorWithContext
import com.cognifide.slice.api.provider.ModelProvider
import com.cognifide.slice.api.qualifier.CurrentResourcePathModel
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
import org.apache.sling.commons.testing.osgi.MockBundleContext
import org.junit.Assert
import org.osgi.framework.ServiceReference
import org.osgi.framework.ServiceRegistration
import spock.lang.Shared

import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * @author Jaromir Celejewski
 * Date: 10.04.15
 */
class SliceTagUtilsTest extends ProsperSpec{

    @Shared
    InjectorHierarchy injectorHierarchy

    @Shared
    InjectorsRepositoryService repositoryService

    @Shared
    protected Injector injector1

    @Shared
    protected Injector injector2

    @Shared
    protected ModelProvider modelProvider

    private static final String BUNDLE_NAME_FILTER = "slice-test-app.*";

    private static final String BASE_PACKAGE = "com.cognifide.example";

    private static final String INJECTOR_NAME = "slice-test";

    def setup() {
        ContextScope contextScope = new SliceContextScope()
        List<Module> modules = new ArrayList<Module>()
        modules.add(new SliceModule(contextScope, null))
        modules.add(new SlingModule(contextScope))
        modules.add(new JcrModule())
        modules.add(new MapperModule())
        modules.add(new SliceResourceModule())
        modules.add(new TestModule())

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

        repositoryService = new InjectorsRepositoryService()
        injectorHierarchy = new InjectorHierarchy()
        injectHierarchyIntoRepositoryService(repositoryService, injectorHierarchy)

        Method bindConfigMethod = injectorHierarchy.getClass().getDeclaredMethod("bindConfig", InjectorConfig.class)
        bindConfigMethod.setAccessible(true)
        bindConfigMethod.invoke(injectorHierarchy, config)
        bindConfigMethod.invoke(injectorHierarchy, config2)
        bindConfigMethod.invoke(injectorHierarchy, config3)

        //ContextFactory factory = injector2.getInstance(ContextFactory.class)
        //Context context = factory.getResourceResolverContext(resourceResolver)
        //contextScope.setContextProvider(new ConstantContextProvider(context))

        //modelProvider = injector.getInstance(ModelProvider.class)
    }

    def "Has injector"() {
        expect:
        Injector injector = injectorHierarchy.getInjectorByName("slice-test")
        Assert.assertNotNull(injector)
        Injector injectorNonExistent = injectorHierarchy.getInjectorByName("slice-xxx")
        Assert.assertNull(injectorNonExistent)
        Injector injector2 = injectorHierarchy.getInjectorByName("slice-test2")
        Assert.assertNotNull(injector2)
        Injector injector3 = injectorHierarchy.getInjectorByName("slice-test/subtest")
        Assert.assertNotNull(injector3)
        String injector3name = injectorHierarchy.getInjectorNameByApplicationPath("/apps/slice-test/subtest")
        Assert.assertEquals("slice-test/subtest", injector3name)
        String injector3regName = injectorHierarchy.getRegisteredName(injector3)
        Assert.assertEquals("slice-test/subtest", injector3regName)
        Assert.assertTrue(injectorHierarchy.getInjectorNames().contains("slice-test"))
        Assert.assertTrue(injectorHierarchy.getInjectorNames().contains("slice-test2"))
        Assert.assertTrue(injectorHierarchy.getInjectorNames().contains("slice-test/subtest"))
        Assert.assertFalse(injectorHierarchy.getInjectorNames().contains("slice-test/subtestx"))

        String injectorNameForRes1 = repositoryService.getInjectorNameForResource("slice-test/abc/abc")
        Assert.assertEquals("slice-test",injectorNameForRes1)
        String injectorNameForRes2 = repositoryService.getInjectorNameForResource("slice-test/subtest/abc")
        Assert.assertEquals("slice-test/subtest",injectorNameForRes2)
        String injectorNameForResNull = repositoryService.getInjectorNameForResource("slice-test3/subtest/abc")
        Assert.assertNull(injectorNameForResNull)

        InjectorWithContext injectorWithContext1 = repositoryService.getInjector("slice-test/subtest")
        Assert.assertNotNull(injectorWithContext1)
        InjectorWithContext injectorWithContext2 = repositoryService.getInjector("slice-test/subtestx")
        Assert.assertNull(injectorWithContext2)
    }


    private void injectHierarchyIntoRepositoryService(InjectorsRepositoryService repositoryService, InjectorHierarchy injectorHierarchy) {
        Field f = repositoryService.getClass().getDeclaredField("injectors")
        f.setAccessible(true)
        f.set(repositoryService, injectorHierarchy)
    }

}
