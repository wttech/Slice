package com.cognifide.slice.core.internal.injector

import com.cognifide.slice.api.injector.InjectorWithContext
import org.junit.Assert

/**
 * Created by T530 on 2015-04-21.
 */
class InjectorsRepositoryServiceTestSetup extends InjectorsTestSetup{

    def "Find name of proper injector for given resource path"() {
        given: "defined injectors: 'slice-test', 'slice-test2', 'slice-test/subtest'"

        when:
        String injectorNameForRes1 = repositoryService.getInjectorNameForResource("slice-test/abc/abc")
        String injectorNameForRes2 = repositoryService.getInjectorNameForResource("slice-test/subtest/abc")
        String injectorNameForRes3 = repositoryService.getInjectorNameForResource("slice-test3/subtest/abc")
        String injectorNameForResNull = repositoryService.getInjectorNameForResource(null)

        then: "find best matching injector for given resource path or return null if no matching injector found"
        Assert.assertEquals("slice-test",injectorNameForRes1)
        Assert.assertEquals("slice-test/subtest",injectorNameForRes2)
        Assert.assertNull(injectorNameForRes3)
        Assert.assertNull(injectorNameForResNull)
    }

    def "Get InjectorWithContext for given injector name"() {
        given: "defined injectors: 'slice-test', 'slice-test2', 'slice-test/subtest'"

        when:
        InjectorWithContext injectorWithContext1 = repositoryService.getInjector("slice-test/subtest")
        InjectorWithContext injectorWithContext2 = repositoryService.getInjector("slice-test")
        InjectorWithContext injectorWithContext3 = repositoryService.getInjector("slice-test2")
        InjectorWithContext injectorWithContext4 = repositoryService.getInjector("slice-test/subtestx")
        InjectorWithContext injectorWithContext5 = repositoryService.getInjector(null)

        then: "return non null InjectorWithContext whenever injector with a given name exists"
        Assert.assertNotNull(injectorWithContext1)
        Assert.assertNotNull(injectorWithContext2)
        Assert.assertNotNull(injectorWithContext3)
        Assert.assertNull(injectorWithContext4)
        Assert.assertNull(injectorWithContext5)
    }
}
