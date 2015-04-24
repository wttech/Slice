package com.cognifide.slice.mapper.annotation

import com.cognifide.slice.test.setup.BaseSetup
import com.google.inject.ProvisionException
import org.junit.Assert

/**
 * @author Mariusz Kubiś Date: 14.04.15
 */
class ChildrenTest extends BaseSetup {

    def "Children test"() {
        setup:
        pageBuilder.content {
            test("cq:PageContent") {
                "jcr:content"("text": "Test") {
                    "children"("sling:Folder") {
                        "item"("text": "Test1", "style": "Style1", "size": 1)
                        "item_0"("text": "Test2", "style": "Style2", "size": 2)
                    }
                }
            }
        }

        ChildrenModel childrenModel = modelProvider.get(ChildrenModel.class, "/content/test/jcr:content")

        expect:
        Assert.assertEquals(childrenModel.getText(), "Test")
        Assert.assertEquals(childrenModel.getChildrenList().size(), 2)

        JcrPropertyModel item = childrenModel.getChildrenList().get(0)
        checkJcrPropertyModel(item, "Test1", "Style1", 1)

        JcrPropertyModel item_0 = childrenModel.getChildrenList().get(1)
        checkJcrPropertyModel(item_0, "Test2", "Style2", 2)
    }

     def "Children test with path starting with '/'"() {
        setup: "Creating initial content"
        pageBuilder.content {
            test("cq:PageContent") {
                "jcr:content"("text": "Test") {
                    "children"("sling:Folder") {
                        "item"("text": "Test1", "style": "Style1", "size": 1)
                        "item_0"("text": "Test2", "style": "Style2", "size": 2)
                    }
                }
            }
        }
        when: "Get a model with invalid annotation value - it starts with '/'"
        modelProvider.get(ChildrenModelWithInvalidReference.class, "/content/test/jcr:content")

        then: "Throw Provision Exception"
        thrown(ProvisionException)
    }

    def "Children test with invalid children type defined in annotation"() {
        setup: "Creating initial content"
        pageBuilder.content {
            test("cq:PageContent") {
                "jcr:content"("text": "Test") {
                    "children"("sling:Folder") {
                        "item"("text": "Test1", "style": "Style1", "size": 1)
                        "item_0"("text": "Test2", "style": "Style2", "size": 2)
                    }
                }
            }
        }
        when: "Get a model with invalid children type defined in annotation"
        modelProvider.get(ChildrenModelWithInvalidChildrenClass.class, "/content/test/jcr:content")

        then: "Throw Provision Exception"
        thrown(ProvisionException)
    }

    def "Children test (mapping to array)"() {
        setup:
        pageBuilder.content {
            test("cq:PageContent") {
                "jcr:content"("text": "Test") {
                    "children"("sling:Folder") {
                        "item"("text": "Test1", "style": "Style1", "size": 1)
                        "item_0"("text": "Test2", "style": "Style2", "size": 2)
                    }
                }
            }
        }

        ChildrenModel childrenModel = modelProvider.get(ChildrenModel.class, "/content/test/jcr:content")

        expect:
        Assert.assertEquals("Test", childrenModel.getText())
        Assert.assertEquals(2, childrenModel.getChildrenArray().length)

        JcrPropertyModel item = childrenModel.getChildrenArray()[0]
        checkJcrPropertyModel(item, "Test1", "Style1", 1)

        JcrPropertyModel item_0 = childrenModel.getChildrenArray()[1]
        checkJcrPropertyModel(item_0, "Test2", "Style2", 2)
    }

    def "Children test with non-existing children resource"() {
        setup:
        pageBuilder.content {
            test1("cq:PageContent") {
                "jcr:content"("text": "Test") {
                }
            }
        }

        ChildrenModel childrenModel = modelProvider.get(ChildrenModel.class, "/content/test1/jcr:content")

        expect:
        Assert.assertEquals(childrenModel.getText(), "Test")
        Assert.assertEquals(childrenModel.getChildrenList().size(), 0)
    }

    def "Children test with empty children items list"() {
        setup:
        pageBuilder.content {
            test2("cq:PageContent") {
                "jcr:content"("text": "Test") {
                    "children"("sling:Folder") {
                    }
                }
            }
        }

        ChildrenModel childrenModel = modelProvider.get(ChildrenModel.class, "/content/test2/jcr:content")

        expect:
        Assert.assertEquals(childrenModel.getText(), "Test")
        Assert.assertEquals(childrenModel.getChildrenList().size(), 0)
    }

    def "Children and Follow test"() {
        setup:
        pageBuilder.content {
            foo("cq:PageContent") {
                "jcr:content"() {
                    "item"("text": "Test1", "style": "Style1", "size": 1)
                    "item_0"("text": "Test2", "style": "Style2", "size": 2)
                }
            }
        }

        pageBuilder.content {
            bar("cq:PageContent") {
                "jcr:content"("text": "Test", "children": "/content/foo/jcr:content")
            }
        }

        ChildrenFollowModel childrenFollowModel = modelProvider.get(ChildrenFollowModel.class, "/content/bar/jcr:content")

        expect:
        Assert.assertEquals(childrenFollowModel.getText(), "Test")
        Assert.assertEquals(childrenFollowModel.getChildrenList().size(), 2)

        JcrPropertyModel item = childrenFollowModel.getChildrenList().get(0)
        checkJcrPropertyModel(item, "Test1", "Style1", 1)

        JcrPropertyModel item_0 = childrenFollowModel.getChildrenList().get(1)
        checkJcrPropertyModel(item_0, "Test2", "Style2", 2)
    }

    private static void checkJcrPropertyModel(JcrPropertyModel model, String text, String secondProperty, int size) {
        Assert.assertNotNull(model)
        Assert.assertEquals(model.getText(), text)
        Assert.assertEquals(model.getSecondProperty(), secondProperty)
        Assert.assertEquals(model.getSize(), size)
    }
}
