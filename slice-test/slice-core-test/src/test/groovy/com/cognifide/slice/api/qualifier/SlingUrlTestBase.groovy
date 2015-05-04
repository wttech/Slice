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

import com.citytechinc.aem.prosper.specs.ProsperSpec
import com.cognifide.slice.api.context.ConstantContextProvider
import com.cognifide.slice.api.context.Context
import com.cognifide.slice.api.context.ContextFactory
import com.cognifide.slice.api.context.ContextScope
import com.cognifide.slice.api.provider.ModelProvider
import com.cognifide.slice.core.internal.context.SliceContextScope
import com.cognifide.slice.core.internal.module.JcrModule
import com.cognifide.slice.core.internal.module.SliceModule
import com.cognifide.slice.core.internal.module.SliceResourceModule
import com.cognifide.slice.core.internal.module.SlingModule
import com.cognifide.slice.mapper.module.MapperModule
import com.cognifide.slice.test.module.TestModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.api.SlingHttpServletResponse
import spock.lang.Shared

/**
 * Created by Jaromir Celejewski on 2015-04-22.
 */
class SlingUrlTestBase extends ProsperSpec {

	@Shared
	SlingHttpServletRequest request

	@Shared
	SlingHttpServletResponse response

	@Shared
	ModelProvider modelProvider

	@Shared
	SlingModule slingModule

	@Shared
	Injector injector

	@Shared
	ContextScope contextScope

	def setupSpec() {
		contextScope = new SliceContextScope()
		List<Module> modules = new ArrayList<Module>()
		modules.add(new SliceModule(contextScope, null))
		modules.add(slingModule = new SlingModule(contextScope))
		modules.add(new JcrModule())
		modules.add(new MapperModule())
		modules.add(new SliceResourceModule())
		modules.add(new TestModule())

		injector = Guice.createInjector(modules)
	}

	def richRequestContext() {
		request = requestBuilder.build {
			parameters = [path: "/content/prosper"]
			selectors = ["one", "two"]
			contentType = "application/json"
			extension = "html"
			suffix = "suff"
		}
		response = responseBuilder.build()
		initContext(request, response)
	}

	def simpleRequestContext() {
		request = requestBuilder.build {
			parameters = [path: "/content/prosper"]
			contentType = "application/json"
		}
		response = responseBuilder.build()
		initContext(request, response)
	}

	def emptyRequestContext() {
		request = requestBuilder.build()
		response = responseBuilder.build()
		initContext(request, response)
	}

	def initContext(request, response) {
		ContextFactory factory = injector.getInstance(ContextFactory.class)

		Context context = factory.getServletRequestContext("injector-name", request, response)
		contextScope.setContextProvider(new ConstantContextProvider(context))

		modelProvider = injector.getInstance(ModelProvider.class)
	}
}
