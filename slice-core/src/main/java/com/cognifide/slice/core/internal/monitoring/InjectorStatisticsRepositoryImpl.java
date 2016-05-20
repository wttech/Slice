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

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import com.cognifide.slice.api.execution.ExecutionContext;
import com.cognifide.slice.api.injector.InjectorNameProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class InjectorStatisticsRepositoryImpl implements InjectorStatisticsRepository {

	private final Map<String, ModelUsageData> injectionsStatistics = new HashMap<String, ModelUsageData>();

	private final String injectorName;
	
	@Inject
	public InjectorStatisticsRepositoryImpl(InjectorNameProvider nameProvider) {
		injectorName = nameProvider.getInjectorName();
	}

	@Override
	public InjectionMonitoringContext startMonitoring() {
		return new InjectionMonitoringContext();
	}

	@Override
	public void save(InjectionMonitoringContext ctx) {
		saveEvent(ctx.getInjecteeClass(), ctx.getElapsedTime(), ctx.getModelsStack());
	}

	private synchronized void saveEvent(Class<?> type, Long timeMeasurement,
			Queue<ExecutionContext> modelHierarchyContext) {
		ModelUsageData properItemInHierarchy = getInjectorStatisticsRoot(injectorName);
		while (modelHierarchyContext.peek() != null) {
			Class<?> ctx = modelHierarchyContext.poll().getInjecteeClass();
			if (!properItemInHierarchy.containsKey(ctx)) {
				properItemInHierarchy.put(ctx, new ModelUsageData());
			}
			properItemInHierarchy = properItemInHierarchy.get(ctx);
		}
		properItemInHierarchy.addTimeMeasurement(timeMeasurement);
	}

	private ModelUsageData getInjectorStatisticsRoot(String injectorName) {
		ModelUsageData root = injectionsStatistics.get(injectorName);
		if (root == null) {
			root = new ModelUsageData();
			injectionsStatistics.put(injectorName, root);
		}
		return root;
	}
	
	public synchronized Map<String, ModelUsageData> getStatistics() {
		return new HashMap<String, ModelUsageData>(injectionsStatistics);
	}
	
	public synchronized void clearHistory() {
		injectionsStatistics.clear();
	}
}