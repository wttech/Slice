/*-
 * #%L
 * Slice - Tests Base
 * %%
 * Copyright (C) 2012 Wunderman Thompson Technology
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
