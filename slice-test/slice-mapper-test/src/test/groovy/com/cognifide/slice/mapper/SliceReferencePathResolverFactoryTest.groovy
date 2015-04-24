package com.cognifide.slice.mapper

import com.cognifide.slice.mapper.api.SliceReferencePathResolver
import com.cognifide.slice.test.setup.BaseSetup
import org.junit.Assert

/**
 * @author Mariusz Kubiś Date: 24.04.15
 */
class SliceReferencePathResolverFactoryTest extends BaseSetup {

    def "Create a Slice Reference Path Resolver by Slice Reference Path Resolver Factory"() {
        SliceReferencePathResolver referencePathResolver = SliceReferencePathResolverFactory.createResolver(injector)
        expect: "Slice Reference Path Resolver was correctly created"
        Assert.assertNotNull(referencePathResolver);
    }

}
