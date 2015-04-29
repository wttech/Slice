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
package com.cognifide.slice.core.provider

import com.cognifide.slice.test.module.SimpleCacheableModel
import com.cognifide.slice.test.module.SimpleCacheablePojo
import com.cognifide.slice.test.module.SimpleCacheablePojoExtendingModel
import com.cognifide.slice.test.module.SimpleModel
import com.cognifide.slice.test.module.SimplePojo
import com.cognifide.slice.test.setup.BaseSetup
import org.junit.Assert

/**
 * Created by T530 on 2015-04-21.
 */
class SliceModelProviderCacheableTest extends BaseSetup {

	def setupSpec() {
		setup: "Create simple, initial content - node 'foo' with property 'prop1' set to 'prop1Value'"
		pageBuilder.content {
			foo("foo") { "jcr:content"("prop1": "prop1Value") }
		}
	}

	def "Get non cacheable model by class and path"() {
		def path = "/content/foo/jcr:content"

		setup: "Creating model for path: " << path
		SimpleModel modelA = modelProvider.get(SimpleModel.class, path);
		SimpleModel modelB = modelProvider.get(SimpleModel.class, path);

		expect: "Model should not be null"
		Assert.assertNotNull(modelA)
		Assert.assertNotNull(modelB)

		and: "Model provider should deliver new model instance on each request"
		Assert.assertTrue(modelA!=modelB)
	}

	def "Get cacheable model by class and path"() {
		def path = "/content/foo/jcr:content"

		setup: "Creating model for path: " << path

		SimpleCacheableModel modelA = modelProvider.get(SimpleCacheableModel.class, path);
		SimpleCacheableModel modelB = modelProvider.get(SimpleCacheableModel.class, path);

		expect: "Model should not be null"
		Assert.assertNotNull(modelA)
		Assert.assertNotNull(modelB)

		and: "Model provider should deliver the same model instance on each request"
		Assert.assertTrue(modelA==modelB)
	}

	def "Get non cacheable pojo by class and path"() {
		def path = "/content/foo/jcr:content"

		setup: "Creating model for path: " << path
		SimplePojo modelA = modelProvider.get(SimplePojo.class, path);
		SimplePojo modelB = modelProvider.get(SimplePojo.class, path);

		expect: "Model should not be null"
		Assert.assertNotNull(modelA)
		Assert.assertNotNull(modelB)

		and: "Model provider should deliver new pojo instance on each request"
		Assert.assertTrue(modelA!=modelB)
	}

	def "Get cacheable pojo by class and path"() {
		def path = "/content/foo/jcr:content"

		setup: "Creating model for path: " << path

		SimpleCacheablePojo modelA = modelProvider.get(SimpleCacheablePojo.class, path);
		SimpleCacheablePojo modelB = modelProvider.get(SimpleCacheablePojo.class, path);

		expect: "Model should not be null"
		Assert.assertNotNull(modelA)
		Assert.assertNotNull(modelB)

		and: "Model provider should deliver the same pojo instance on each request"
		Assert.assertTrue(modelA==modelB)
	}

	def "Get cacheable pojo by class for null path"() {
		def path = "/content/foo/jcr:content"

		setup: "Creating model for path: null"

		SimpleCacheablePojo modelA = modelProvider.get(SimpleCacheablePojo.class, null);
		SimpleCacheablePojo modelB = modelProvider.get(SimpleCacheablePojo.class, null);

		expect: "Model should not be null"
		Assert.assertNotNull(modelA)
		Assert.assertNotNull(modelB)

		and: "Model provider should deliver the same pojo instance on each request"
		Assert.assertTrue(modelA==modelB)
	}

	def "Get cacheable pojos for different paths"() {
		def path1 = "/content/foo/jcr:content"
		def path2 = "/content/foo2/jcr:content"

		setup: "Creating model for path '" << path1 << "' and '" << path2 << "'"

		SimpleCacheablePojo modelA = modelProvider.get(SimpleCacheablePojo.class, path1);
		SimpleCacheablePojo modelB = modelProvider.get(SimpleCacheablePojo.class, path2);
		SimpleCacheablePojo modelC = modelProvider.get(SimpleCacheablePojo.class, null);

		expect: "Model should not be null"
		Assert.assertNotNull(modelA)
		Assert.assertNotNull(modelB)
		Assert.assertNotNull(modelC)

		and: "Model provider should deliver different pojo instance on each request"
		Assert.assertTrue(modelA!=modelB)
		Assert.assertTrue(modelB!=modelC)
		Assert.assertTrue(modelC!=modelA)
	}

	def "Get cacheable classes of inheriting types for the same path"() {
		def path = "/content/foo/jcr:content"

		setup: "Creating models for path '" << path

		SimpleCacheableModel modelA = modelProvider.get(SimpleCacheablePojoExtendingModel.class, path);
		SimpleCacheableModel modelB = modelProvider.get(SimpleCacheableModel.class, path);

		expect: "Model should not be null"
		Assert.assertNotNull(modelA)
		Assert.assertNotNull(modelB)

		and: "Model provider should deliver different instances"
		Assert.assertTrue(modelA!=modelB)
	}
}
