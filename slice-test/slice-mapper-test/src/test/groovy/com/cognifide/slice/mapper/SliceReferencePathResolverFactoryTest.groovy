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
