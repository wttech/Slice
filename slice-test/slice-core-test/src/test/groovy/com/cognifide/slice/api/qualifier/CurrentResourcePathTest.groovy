/*-
 * #%L
 * Slice - Core Tests
 * %%
 * Copyright (C) 2012 Cognifide Limited
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.cognifide.slice.api.qualifier

import com.cognifide.slice.test.setup.BaseSetup
import org.apache.sling.api.resource.Resource
import org.junit.Assert

/**
 * @author Mariusz Kubiś
 * Date: 10.04.15
 */
class CurrentResourcePathTest extends BaseSetup {

    private String path = "/content/testPath/jcr:content"

    def "Get current resource path model by resource"() {
        setup: "Initialize context"
        pageBuilder.content {
            testPath("cq:PageContent") {
                "jcr:content"("text": "Test", "style": "Style")
            }
        }
        when:
        Resource resource = getResource(path)
        CurrentResourcePathModel currentResourcePathModel = modelProvider.get(CurrentResourcePathModel.class, resource)

        then: "Resource exists and model has been properly initialized"
        Assert.assertNotNull(resource)
        Assert.assertNotNull(currentResourcePathModel)
        Assert.assertEquals(currentResourcePathModel.getCurrentResourcePath(), path)
    }

    def "Get current resource path model by path"() {
        given: "Example content"
        when:
        CurrentResourcePathModel currentResourcePathModel = modelProvider.get(CurrentResourcePathModel.class, path)
        then: "Model has been property initialized"
        Assert.assertNotNull(currentResourcePathModel)
        Assert.assertEquals(currentResourcePathModel.getCurrentResourcePath(), path)
    }
}
