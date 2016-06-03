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
package com.cognifide.slice.core.internal.monitoring;

import java.lang.reflect.Field
import java.util.concurrent.TimeUnit;

import org.junit.Assert
import org.mockito.Mockito
import org.springframework.test.annotation.DirtiesContext;

import com.citytechinc.aem.prosper.specs.ProsperSpec;
import com.cognifide.slice.api.injector.InjectorConfig
import com.cognifide.slice.api.injector.InjectorRunner
import com.cognifide.slice.api.provider.ModelProvider
import com.cognifide.slice.core.internal.injector.InjectorHierarchy
import com.cognifide.slice.core.internal.provider.SliceModelProvider
import com.cognifide.slice.test.module.CompositeModel
import com.cognifide.slice.test.module.SimpleModel
import com.cognifide.slice.test.setup.BaseSetup

class ModelUsageDataTest extends ProsperSpec {

	private ModelUsageData modelUsageData;

	def setup() {
		this.modelUsageData = new ModelUsageData();
	}

	def "Add other ModelUsagaData objects"() {
		setup:
		def anotherModelUsageData = new ModelUsageData();
		anotherModelUsageData.addTimeMeasurement(TimeUnit.MILLISECONDS.toNanos(5));

		this.modelUsageData.addTimeMeasurement(TimeUnit.MILLISECONDS.toNanos(10));
		this.modelUsageData.add(anotherModelUsageData);

		expect:
		Assert.assertEquals("Count", 2, this.modelUsageData.getCount());
		Assert.assertEquals("Total time", 15, this.modelUsageData.getTotalTimeInMillis());
	}

	def "Compute avarage times"() {
		expect:
		this.modelUsageData.addTimeMeasurement(TimeUnit.MILLISECONDS.toNanos(10));
		Assert.assertEquals("Average", 10.0, this.modelUsageData.getAverageTimeInMillis(), 0.001);

		this.modelUsageData.addTimeMeasurement(TimeUnit.MILLISECONDS.toNanos(30));
		Assert.assertEquals("Average", 20.0, this.modelUsageData.getAverageTimeInMillis(), 0.001);

		this.modelUsageData.addTimeMeasurement(TimeUnit.MILLISECONDS.toNanos(5));
		Assert.assertEquals("Average", 15.0, this.modelUsageData.getAverageTimeInMillis(), 0.001);
	}

	def "Copy model"() {
		setup:
		this.modelUsageData.addTimeMeasurement(TimeUnit.MILLISECONDS.toNanos(10));
		Assert.assertEquals("Average", 10.0, this.modelUsageData.getAverageTimeInMillis(), 0.001);

		def ModelUsageData copiedModelUsageData = this.modelUsageData.copy();

		expect:
		Assert.assertEquals("Count", 1, copiedModelUsageData.getCount());
		Assert.assertEquals("Total time", 10, copiedModelUsageData.getTotalTimeInMillis());
	}
}
