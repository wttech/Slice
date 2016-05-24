/*-
 * #%L
 * Slice - Core
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

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import com.cognifide.slice.api.annotation.OsgiService;
import com.cognifide.slice.api.execution.ExecutionContext;
import com.google.inject.Singleton;

@Singleton
public class InjectorStatisticsRepositoryImpl implements InjectorStatisticsRepository {

	private final ModelUsageData modelUsageDataRoot = new ModelUsageData();
	
	@OsgiService
	private MonitoringConfiguration config;

	@Override
	public InjectionMonitoringContext startMonitoring() {
		return new InjectionMonitoringContext();
	}

	@Override
	public void save(InjectionMonitoringContext ctx) {
		if (config.isMonitoringEnabled()) {
			saveEvent(ctx.getElapsedTime(), ctx.getModelsStack());
		}
	}

	private void saveEvent(Long timeMeasurement, Queue<ExecutionContext> modelHierarchyContext) {
		ModelUsageData properItemInHierarchy = modelUsageDataRoot;
		while (modelHierarchyContext.peek() != null) {
			Class<?> ctx = modelHierarchyContext.poll().getInjecteeClass();
			ConcurrentHashMap<Class<?>, ModelUsageData> subModels = properItemInHierarchy.getSubModels();
			if (!subModels.containsKey(ctx)) {
				subModels.putIfAbsent(ctx, new ModelUsageData());
			}
			properItemInHierarchy = subModels.get(ctx);
		}
		properItemInHierarchy.addTimeMeasurement(timeMeasurement);
	}

	public ModelUsageData getStatistics() {
		return modelUsageDataRoot;
	}

	public void clear() {
		modelUsageDataRoot.clearSubModels();
	}
}