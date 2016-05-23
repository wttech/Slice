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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cognifide.slice.api.injector.InjectorConfig;
import com.cognifide.slice.core.internal.injector.InjectorHierarchy;
import com.google.inject.Injector;

public class SliceStatistics {

	private Map<String, ModelUsageData> modelUsageData = new HashMap<String, ModelUsageData>();

	private SliceStatistics(Map<String, ModelUsageData> modelUsageData) {
		this.modelUsageData = modelUsageData;
	}

	public static SliceStatistics fromInjectors(InjectorHierarchy injectorHierarchy) {
		Map<String, ModelUsageData> statsHistory = new HashMap<String, ModelUsageData>();

		for (String injectorName : injectorHierarchy.getInjectorNames()) {
			Map<String, ModelUsageData> injectorStats = getStatistics(injectorHierarchy, injectorName);
			String injectorFullName = resolveInjectoNameInheritanceStructure(injectorName, injectorHierarchy);

			if (!injectorStats.isEmpty()) {
				statsHistory.put(injectorFullName, injectorStats.get(injectorName));
			}
		}

		return new SliceStatistics(statsHistory);
	}

	private static Map<String, ModelUsageData> getStatistics(InjectorHierarchy injectorHierarchy, String injectorName) {
		Injector injector = injectorHierarchy.getInjectorByName(injectorName);
		InjectorStatisticsRepository statisticsRepository = injector.getInstance(InjectorStatisticsRepository.class);
		return statisticsRepository.getStatistics();
	}

	private static String resolveInjectoNameInheritanceStructure(String injectorName,
			InjectorHierarchy injectorHierarchy) {
		Deque<String> injectorNamesStructure = new ArrayDeque<String>();

		String currentInjectorName = injectorName;
		injectorNamesStructure.push(currentInjectorName);

		InjectorConfig currentInjectorConfig = injectorHierarchy.getInjectorConfigByName(currentInjectorName);
		while (currentInjectorConfig.hasParent()) {
			currentInjectorName = currentInjectorConfig.getParentName();
			injectorNamesStructure.push(currentInjectorName);

			currentInjectorConfig = injectorHierarchy.getInjectorConfigByName(currentInjectorName);
		}

		return StringUtils.join(injectorNamesStructure, " > ");
	}

	public Map<String, ModelUsageData> getStatistics() {
		return modelUsageData;
	}

	public boolean isEmpty() {
		return getStatistics().isEmpty();
	}
}