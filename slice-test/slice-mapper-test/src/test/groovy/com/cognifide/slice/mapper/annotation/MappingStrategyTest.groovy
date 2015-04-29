/*-
 * #%L
 * Slice - Mapper Tests
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
package com.cognifide.slice.mapper.annotation

import com.cognifide.slice.test.setup.BaseSetup
import org.junit.Assert

/**
 * @author Mariusz Kubiś Date: 15.04.15
 */
class MappingStrategyTest extends BaseSetup {

    def "Mapping Strategy Test"() {
        setup:
        pageBuilder.content {
            foo("cq:PageContent") {
                "jcr:content"("field1": "field1 value", "field2": "field2 value", "field3": "field3 value")
            }
        }
        expect:
        assertPageExists("/content/foo")
    }

    def "Mapping Strategy All Test"() {
        expect:
        MappingStrategyAllModel mappingStrategyAllModel = modelProvider.get(MappingStrategyAllModel.class, "/content/foo/jcr:content")
        Assert.assertNotNull(mappingStrategyAllModel)
        Assert.assertEquals(mappingStrategyAllModel.getField1(), "field1 value")
        Assert.assertEquals(mappingStrategyAllModel.getField2(), "field2 value")
        Assert.assertEquals(mappingStrategyAllModel.getField3(), "field3 value")
    }

    def "Mapping Strategy Annotated Test"() {
        expect:
        MappingStrategyAnnotatedModel mappingStrategyAnnotatedModel = modelProvider.get(MappingStrategyAnnotatedModel.class, "/content/foo/jcr:content")
        Assert.assertNotNull(mappingStrategyAnnotatedModel)
        Assert.assertEquals(mappingStrategyAnnotatedModel.getField1(), "field1 value")
        Assert.assertNull(mappingStrategyAnnotatedModel.getField2())
        Assert.assertEquals(mappingStrategyAnnotatedModel.getField3(), "field3 value")
    }
}
