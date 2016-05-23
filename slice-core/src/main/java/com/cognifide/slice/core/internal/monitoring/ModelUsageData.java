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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ModelUsageData implements Map<Class<?>, ModelUsageData> {

	private final Map<Class<?>, ModelUsageData> subModels = new ConcurrentHashMap<Class<?>, ModelUsageData>();

	private AtomicLong count = new AtomicLong();

	private AtomicLong totalTime = new AtomicLong();

	public void addTimeMeasurement(Long timeMeasurement) {
		count.incrementAndGet();
		totalTime.addAndGet(timeMeasurement);
	}

	public long getCount() {
		return count.get();
	}

	public long getTotalTime() {
		return TimeUnit.NANOSECONDS.toMillis(totalTime.get());
	}

	public double getAverageTime() {
		return totalTime.get() / (double) count.get() / 1000000;
	}

	public void add(ModelUsageData statistics) {
		this.count.addAndGet(statistics.count.get());
		this.totalTime.addAndGet(statistics.totalTime.get());
	}

	public ModelUsageData copy() {
		ModelUsageData copy = new ModelUsageData();
		copy.count.set(this.count.get());
		copy.totalTime.set(this.totalTime.get());
		return copy;
	}

	@Override
	public int size() {
		return subModels.size();
	}

	@Override
	public boolean isEmpty() {
		return subModels.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return subModels.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return subModels.containsValue(value);
	}

	@Override
	public ModelUsageData get(Object key) {
		return subModels.get(key);
	}

	@Override
	public ModelUsageData put(Class<?> key, ModelUsageData value) {
		return subModels.put(key, value);
	}

	@Override
	public ModelUsageData remove(Object key) {
		return subModels.remove(key);
	}

	@Override
	public void putAll(Map<? extends Class<?>, ? extends ModelUsageData> m) {
		subModels.putAll(m);
	}

	@Override
	public void clear() {
		subModels.clear();
	}

	@Override
	public Set<Class<?>> keySet() {
		return subModels.keySet();
	}

	@Override
	public Collection<ModelUsageData> values() {
		return subModels.values();
	}

	@Override
	public Set<java.util.Map.Entry<Class<?>, ModelUsageData>> entrySet() {
		return subModels.entrySet();
	}

	@Override
	public boolean equals(Object o) {
		return subModels.equals(o);
	}

	@Override
	public int hashCode() {
		return subModels.hashCode();
	}

}