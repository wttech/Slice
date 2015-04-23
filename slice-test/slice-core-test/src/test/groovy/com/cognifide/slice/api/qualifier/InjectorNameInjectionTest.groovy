package com.cognifide.slice.api.qualifier

import com.cognifide.slice.core.internal.injector.InjectorsTestSetup
import com.google.inject.Injector
import junit.framework.Assert
import spock.lang.Shared


/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/22/15 1:58 PM
 */
class InjectorNameInjectionTest extends InjectorsTestSetup {
    
    @Shared
    Injector injector

    /**
     * Since RepositoryService is a OSGi service and it's being injected by Peaberry, test invokes getInjectorName method
     * directly.
     */
    def "Get Injector Name"() {
        setup: "Get Injector name from SliceModule"
        injector = injectorHierarchy.getInjectorByName("slice-test")
        def injectorName = sliceModule.getInjectorName(repositoryService, injector)

        expect: "Injector name is not null"
        Assert.assertNotNull(injectorName)

        and: "Injector name is equal to 'injector-name'"
        Assert.assertEquals("slice-test", injectorName)
    }

}
