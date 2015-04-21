package com.cognifide.slice.core.internal.injector

import com.cognifide.slice.api.injector.InjectorWithContext
import org.junit.Assert

/**
 * Created by T530 on 2015-04-21.
 */
class InjectorsRepositoryServiceTest extends InjectorsBaseTest{

    def "Find name of proper injector for given resource path"() {
        given: "defined injectors: 'slice-test', 'slice-test2', 'slice-test/subtest'"

        when:

        expect: "Find best matching injectors for given resource path or return null if no matching injector found"
        String injectorNameForRes1 = repositoryService.getInjectorNameForResource("slice-test/abc/abc")
        Assert.assertEquals("slice-test",injectorNameForRes1)
        String injectorNameForRes2 = repositoryService.getInjectorNameForResource("slice-test/subtest/abc")
        Assert.assertEquals("slice-test/subtest",injectorNameForRes2)
        String injectorNameForRes3 = repositoryService.getInjectorNameForResource("slice-test3/subtest/abc")
        Assert.assertNull(injectorNameForRes3)
        String injectorNameForResNull = repositoryService.getInjectorNameForResource(null)
        Assert.assertNull(injectorNameForResNull)
    }

    def "Get InjectorWithContext for given injector name"() {
        given: "defined injectors: 'slice-test', 'slice-test2', 'slice-test/subtest'"

        when:

        expect: "Return non null InjectorWithContext wheever injector with given name exists"
        InjectorWithContext injectorWithContext1 = repositoryService.getInjector("slice-test/subtest")
        Assert.assertNotNull(injectorWithContext1)
        InjectorWithContext injectorWithContext2 = repositoryService.getInjector("slice-test")
        Assert.assertNotNull(injectorWithContext2)
        InjectorWithContext injectorWithContext3 = repositoryService.getInjector("slice-test2")
        Assert.assertNotNull(injectorWithContext3)
        InjectorWithContext injectorWithContext4 = repositoryService.getInjector("slice-test/subtestx")
        Assert.assertNull(injectorWithContext4)
        InjectorWithContext injectorWithContext5 = repositoryService.getInjector(null)
        Assert.assertNull(injectorWithContext5)
    }
}
