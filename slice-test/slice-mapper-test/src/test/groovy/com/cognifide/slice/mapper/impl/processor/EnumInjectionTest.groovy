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
package com.cognifide.slice.mapper.impl.processor

import com.cognifide.slice.mapper.annotation.EnumInjectionModel
import com.cognifide.slice.mapper.annotation.SimpleEnum
import com.cognifide.slice.test.setup.BaseSetup
import junit.framework.Assert

/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 4/23/15 2:01 PM
 */
class EnumInjectionTest extends BaseSetup {

    def "Get a model with a Enum mapped from String (existing Enum value)"() {
        setup: "Create a content with String value (existing Enum value)"
        def path = "/content/foo/jcr:content"
        pageBuilder.content {
            foo("foo") {
                "jcr:content"("enumeration": "VALUE1")
            }
        }
        and: "Get a model"
        def model = modelProvider.get(EnumInjectionModel.class, path)

        expect: "Model is not null"
        Assert.assertNotNull(model)

        and: "Enum property was correctly mapped from String"
        Assert.assertEquals(SimpleEnum.VALUE1, model.getEnumeration())
    }


    def "Get a model with a Enum mapped from String (non-existing Enum value)"() {
        setup: "Create a content with String value (existing Enum value)"
        def path = "/content/foo/jcr:content"
        pageBuilder.content {
            foo("foo") {
                "jcr:content"("enumeration": "NON_EXISTING_VALUE")
            }
        }
        and: "Get a model"
        def model = modelProvider.get(EnumInjectionModel.class, path)

        expect: "Model is not null"
        Assert.assertNotNull(model)

        and: "Enum property was correctly mapped from String"
        Assert.assertEquals(null, model.getEnumeration())
    }

    def "Get a model with a Enum mapped from null"() {
        setup: "Create a content with String value (existing Enum value)"
        def path = "/content/foo/jcr:content"
        pageBuilder.content {
            foo("foo") {
                "jcr:content"("enumeration": null)
            }
        }
        and: "Get a model"
        def model = modelProvider.get(EnumInjectionModel.class, path)

        expect: "Model is not null"
        Assert.assertNotNull(model)

        and: "Enum property was correctly mapped from null"
        Assert.assertEquals(null, model.getEnumeration())
    }

    def "Get a model with a Enum mapped from Boolean"() {
        setup: "Create a content with String value (existing Enum value)"
        def path = "/content/foo/jcr:content"
        pageBuilder.content {
            foo("foo") {
                "jcr:content"("enumeration": true)
            }
        }
        and: "Get a model"
        def model = modelProvider.get(EnumInjectionModel.class, path)

        expect: "Model is not null"
        Assert.assertNotNull(model)

        and: "Enum property was correctly mapped from null"
        Assert.assertEquals(null, model.getEnumeration())
    }


}
