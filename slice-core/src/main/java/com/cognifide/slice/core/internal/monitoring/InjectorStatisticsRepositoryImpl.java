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

import java.util.concurrent.atomic.AtomicBoolean;

import com.google.inject.Singleton;

@Singleton
public class InjectorStatisticsRepositoryImpl implements InjectorStatisticsRepository {

	private final ModelUsageData modelUsageDataRoot = new ModelUsageData();

	private AtomicBoolean enabled = new AtomicBoolean(false);

	public ModelUsageData getModelUsageDataRoot() {
		return this.modelUsageDataRoot;
	}

	public ModelUsageData getStatistics() {
		return modelUsageDataRoot;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled.set(enabled);
	}

	@Override
	public boolean isEnabled() {
		return enabled.get();
	}

	public void clear() {
		modelUsageDataRoot.clearSubModels();
	}
}