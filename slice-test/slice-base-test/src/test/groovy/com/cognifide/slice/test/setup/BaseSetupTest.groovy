package com.cognifide.slice.test.setup

import com.cognifide.slice.api.context.ContextScope
import org.junit.Assert

/**
 * @author Mariusz Kubiś
 * Date: 10.04.15
 */
class BaseSetupTest extends BaseSetup {

    def "model provider is not null"() {
        expect:
        Assert.assertNotNull(modelProvider)
    }

    def "context scope is not null"() {
        expect:
        Assert.assertNotNull(injector.getInstance(ContextScope.class))
    }
}
