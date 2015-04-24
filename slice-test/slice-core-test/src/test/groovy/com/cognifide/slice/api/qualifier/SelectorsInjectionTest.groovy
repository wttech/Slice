package com.cognifide.slice.api.qualifier

import junit.framework.Assert

/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/22/15 11:30 AM
 */
class SelectorsInjectionTest extends InjectionTestBase {

    def "Get Selectors"() {
        setup: "Init model"
        richRequestContext()
        def SelectorsInjectionModel model = modelProvider.get(SelectorsInjectionModel.class, '/content/foo')
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
        richRequestContext()
        def SelectorsInjectionModel model = modelProvider.get(SelectorsInjectionModel.class, '/content/foo')
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
        richRequestContext()
        def SelectorsInjectionModel model = modelProvider.get(SelectorsInjectionModel.class, '/content/foo')
        def selectors = model.getSelectorsAsString()

        expect: "Selectors string is not null"
        Assert.assertNotNull(selectors)

        and: "Selectors string is equal to 'one.two'"
        Assert.assertEquals("one.two", selectors)
    }

    def "Get Selectors for empty request"() {
        setup: "Init model"
        emptyRequestContext()
        def SelectorsInjectionModel model = modelProvider.get(SelectorsInjectionModel.class, '/content/foo')
        def selectors = model.getSelectors()
        def selectorsList = model.getSelectorsAsList()

        expect: "Selectors are not null"
        Assert.assertNotNull(selectors)
        Assert.assertNotNull(selectorsList)

        and: "There are no selectors"
        Assert.assertEquals(0, selectors.size())
        Assert.assertEquals(0, selectorsList.size())
    }

    def "Get Selectors as List for empty request"() {
        setup: "Init model"
        emptyRequestContext()
        def SelectorsInjectionModel model = modelProvider.get(SelectorsInjectionModel.class, '/content/foo')
        def selectors = model.getSelectorsAsList()

        expect: "Selectors are not null"
        Assert.assertNotNull(selectors)

        and: "Selectors are returned as a List"
        Assert.assertTrue(selectors instanceof List)

        and: "There are no selectors in the list"
        Assert.assertEquals(0, selectors.size())
    }

    def "Get Selectors String for empty request"() {
        setup: "Init model"
        emptyRequestContext()
        def SelectorsInjectionModel model = modelProvider.get(SelectorsInjectionModel.class, '/content/foo')
        def selectors = model.getSelectorsAsString()

        expect: "Selectors string is null"
        Assert.assertNull(selectors)
    }
}
