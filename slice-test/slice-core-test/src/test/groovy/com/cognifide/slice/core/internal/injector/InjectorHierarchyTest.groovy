package com.cognifide.slice.core.internal.injector

import com.google.inject.Injector
import org.junit.Assert

/**
 * @author Jaromir Celejewski
 * Date: 10.04.15
 */
class InjectorHierarchyTest extends InjectorsBaseTest{

    def "Find injector by name"() {
        given: "defined injectors: 'slice-test', 'slice-test2', 'slice-test/subtest'"

        when:

        expect: "'slice-test', 'slice-test2', 'slice-test/subtest' exist in injector's hierarchy"
        Injector injector = injectorHierarchy.getInjectorByName("slice-test")
        Assert.assertNotNull(injector)
        Injector injector2 = injectorHierarchy.getInjectorByName("slice-test2")
        Assert.assertNotNull(injector2)
        Injector injector3 = injectorHierarchy.getInjectorByName("slice-test/subtest")
        Assert.assertNotNull(injector3)
    }

    def "Can't find injector by name if it is not created"() {
        given: "defined injectors: 'slice-test', 'slice-test2', 'slice-test/subtest'"

        when:

        expect: "'slice-test3' does not exist in hierarchy"
        Injector injectorNonExistent = injectorHierarchy.getInjectorByName("slice-test3")
        Assert.assertNull(injectorNonExistent)
    }

    def "Can't find injector by null name"() {
        given: "defined injectors: 'slice-test', 'slice-test2', 'slice-test/subtest'"

        when:

        expect: "injector with null name does not exist in hierarchy"
        Injector injectorNonExistent = injectorHierarchy.getInjectorByName(null)
        Assert.assertNull(injectorNonExistent)
    }

    def "Find injector by application path"() {
        given: "defined injectors: 'slice-test', 'slice-test2', 'slice-test/subtest'"

        when:

        expect: "'slice-test', 'slice-test2', 'slice-test/subtest' can be found by path in injector's hierarchy"
        String injector1name = injectorHierarchy.getInjectorNameByApplicationPath("/apps/slice-test")
        Assert.assertEquals("slice-test", injector1name)
        String injector2name = injectorHierarchy.getInjectorNameByApplicationPath("/apps/slice-test2")
        Assert.assertEquals("slice-test2", injector2name)
        String injector3name = injectorHierarchy.getInjectorNameByApplicationPath("/apps/slice-test/subtest")
        Assert.assertEquals("slice-test/subtest", injector3name)
    }

    def "Can't find injector by application path if it is not registered"() {
        given: "defined injectors: 'slice-test', 'slice-test2', 'slice-test/subtest'"

        when:

        expect: "'slice-test3', 'slice-test/subtest2' and null name cannot be found by path in injector's hierarchy"
        String injector1name = injectorHierarchy.getInjectorNameByApplicationPath("/apps/slice-test3")
        Assert.assertNull(injector1name)
        String injector2name = injectorHierarchy.getInjectorNameByApplicationPath("/apps/slice-test/subtest2")
        Assert.assertNull(injector2name)
        String injectorForNullName = injectorHierarchy.getInjectorNameByApplicationPath(null)
        Assert.assertNull(injectorForNullName)
    }

    def "Can find injector by its registered name"() {
        given: "defined injectors: 'slice-test', 'slice-test2', 'slice-test/subtest'"

        when:

        expect: "names of all three given injectors can be found in the hierarchy"
        Injector injector1 = injectorHierarchy.getInjectorByName("slice-test")
        String injector1regName = injectorHierarchy.getRegisteredName(injector1)
        Assert.assertEquals("slice-test", injector1regName)
        Injector injector2 = injectorHierarchy.getInjectorByName("slice-test2")
        String injector2regName = injectorHierarchy.getRegisteredName(injector2)
        Assert.assertEquals("slice-test2", injector2regName)
        Injector injector3 = injectorHierarchy.getInjectorByName("slice-test/subtest")
        String injector3regName = injectorHierarchy.getRegisteredName(injector3)
        Assert.assertEquals("slice-test/subtest", injector3regName)
    }

    def "Collection of names in hierarchy contains only registered injectors"() {
        given: "defined injectors: 'slice-test', 'slice-test2', 'slice-test/subtest'"

        when:

        expect: "Collection of injector's names contains: 'slice-test', 'slice-test2', 'slice-test/subtest'"
                Assert.assertTrue(injectorHierarchy.getInjectorNames().contains("slice-test"))
        Assert.assertTrue(injectorHierarchy.getInjectorNames().contains("slice-test2"))
        Assert.assertTrue(injectorHierarchy.getInjectorNames().contains("slice-test/subtest"))
        Assert.assertFalse(injectorHierarchy.getInjectorNames().contains("slice-test/subtestx"))
        Assert.assertFalse(injectorHierarchy.getInjectorNames().contains(null))
    }
}

