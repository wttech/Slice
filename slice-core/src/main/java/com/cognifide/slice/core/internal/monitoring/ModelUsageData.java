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
import java.util.concurrent.TimeUnit;

public class ModelUsageData extends HashMap<Class<?>, ModelUsageData> {

	private static final long serialVersionUID = -1658020830178783629L;

	private long count;

	private long totalTime;

	public void addTimeMeasurement(Long timeMeasurement) {
		count++;
		totalTime += timeMeasurement;
	}

	public long getCount() {
		return count;
	}

	public long getTotalTime() {
		return TimeUnit.NANOSECONDS.toMillis(totalTime);
	}

	public double getAverageTime() {
		return totalTime / (double) count / 1000000;
	}

	public void add(ModelUsageData statistics) {
		this.count += statistics.count;
		this.totalTime += statistics.totalTime;
	}

	public ModelUsageData copy() {
		ModelUsageData copy = new ModelUsageData();
		copy.count = this.count;
		copy.totalTime = this.totalTime;
		return copy;
	}
}