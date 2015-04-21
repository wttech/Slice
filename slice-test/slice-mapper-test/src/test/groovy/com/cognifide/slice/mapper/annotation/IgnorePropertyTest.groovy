package com.cognifide.slice.mapper.annotation

import com.cognifide.slice.test.setup.BaseSetup
import org.junit.Assert

/**
 * @author Mariusz Kubiś Date: 14.04.15
 */
class IgnorePropertyTest extends BaseSetup {

    def "Ignore property test"() {
        setup:
        pageBuilder.content {
            foo("cq:PageContent") {
                "jcr:content"("text": "Test", "style": "Style", "size": 5)
            }
        }
        def contentNode = session.getNode("/content/foo/jcr:content")
        IgnorePropertyModelAll ignorePropertyModelAll = modelProvider.get(IgnorePropertyModelAll.class,
                "/content/foo/jcr:content")
        IgnorePropertyModelAnnotated ignorePropertyModelAnnotated = modelProvider.get(IgnorePropertyModelAnnotated.class,
                "/content/foo/jcr:content")

        expect:
        Assert.assertEquals(contentNode.get("style"), "Style")
        Assert.assertEquals(contentNode.get("size"), 5)

        Assert.assertNotNull(ignorePropertyModelAll)
        Assert.assertEquals(ignorePropertyModelAll.getText(), "Test")
        Assert.assertEquals(ignorePropertyModelAll.getSize(), 10)
        Assert.assertNull(ignorePropertyModelAll.getStyle())

        Assert.assertNotNull(ignorePropertyModelAnnotated)
        Assert.assertEquals(ignorePropertyModelAnnotated.getText(), "Test")
        Assert.assertEquals(ignorePropertyModelAnnotated.getSize(), 10)
        Assert.assertNull(ignorePropertyModelAnnotated.getStyle())
    }
}
