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

import com.cognifide.slice.api.injector.InjectorWithContext;
import com.cognifide.slice.api.injector.InjectorsRepository;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class SliceStatistics {

	private Map<String, ModelUsageData> modelUsageData = new HashMap<String, ModelUsageData>();

	private SliceStatistics(Map<String, ModelUsageData> modelUsageData) {
		this.modelUsageData = modelUsageData;
	}

	public static SliceStatistics fromInjectors(InjectorsRepository injectorsRepository) {
		Map<String, ModelUsageData> statsHistory = new HashMap<String, ModelUsageData>();
		for (String injectorName : injectorsRepository.getInjectorNames()) {
			InjectorWithContext injector = injectorsRepository.getInjector(injectorName);
			InjectorStatisticsRepository stats = injector.getInstance(InjectorStatisticsRepository.class);
			Map<String, ModelUsageData> injectorStats = stats.getStatistics();
			statsHistory.putAll(injectorStats);
		}
		SliceStatistics injectionsReport = new SliceStatistics(statsHistory);
		injectionsReport.resolveInjectorsHierarchy(injectorsRepository);
		return injectionsReport;
	}

	private void resolveInjectorsHierarchy(InjectorsRepository injectorsRepository) {
		for (String injectorName : modelUsageData.keySet()) {
			Injector currentParent = null;
			Injector injector = injectorsRepository.getInjector(injectorName).getInjector();
			String newName = injectorName;
			do {
				currentParent = injector.getParent();
				if (currentParent != null) {
					newName = injectorsRepository.getInjectorName(currentParent) + '>' + newName;
					injector = currentParent;
				}
			} while (currentParent != null);
			if (!newName.equals(injectorName)) {
				modelUsageData.put(newName, modelUsageData.get(injectorName));
			}
		}
	}

	public Map<String, ModelUsageData> getStatistics() {
		return modelUsageData;
	}

	public boolean isEmpty() {
		return getStatistics().isEmpty();
	}
}