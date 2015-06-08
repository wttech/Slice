/*-
 * #%L
 * Slice - Mapper
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
package com.cognifide.slice.mapper.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.cognifide.slice.mapper.api.processor.FieldPostProcessor;
import com.cognifide.slice.mapper.api.processor.FieldProcessor;
import com.cognifide.slice.mapper.api.processor.PriorityFieldPostProcessor;
import com.cognifide.slice.mapper.api.processor.PriorityFieldProcessor;
import com.cognifide.slice.mapper.api.processor.PriorityProcessor;
import com.google.inject.Inject;

/**
 * A class responsible for collecting custom field processors and post-processors registered with guice's
 * multibinding.
 * 
 * @author maciej.dybek
 */
public class CustomProcessorsCollector {

	@Inject(optional = true)
	private Set<FieldProcessor> customFieldProcessors;

	@Inject(optional = true)
	private Set<FieldPostProcessor> customFieldPostProcessors;

	@Inject(optional = true)
	private Set<PriorityFieldProcessor> priorityCustomFieldProcessors;

	@Inject(optional = true)
	private Set<PriorityFieldPostProcessor> priorityCustomFieldPostProcessors;

	/**
	 * Returns a list of all custom field processors registered with multibindings. Processors are sorted in
	 * the following order:
	 * <ol>
	 * <li>first all {@link PriorityFieldProcessor}s sorted according to the priority</li>
	 * <li>then all {@link FieldProcessor}</li>
	 * </ol>
	 * 
	 * @return
	 */
	public LinkedList<FieldProcessor> getFieldProcessors() {
		LinkedList<FieldProcessor> result = new LinkedList<FieldProcessor>();
		result.addAll(sortByPriority(toList(priorityCustomFieldProcessors)));
		result.addAll(toList(customFieldProcessors));
		return result;
	}

	/**
	 * Returns a list of all custom field post-processors registered with multibindings. Post-processors are
	 * sorted in the following order:
	 * <ol>
	 * <li>first all {@link PriorityFieldPostProcessor}s with positive priority sorted according to the
	 * priority</li>
	 * <li>than all {@link FieldPostProcessor}</li>
	 * </ol>
	 * 
	 * @return
	 */
	public LinkedList<FieldPostProcessor> getHighPriorityFieldPostProcessors() {
		LinkedList<FieldPostProcessor> result = new LinkedList<FieldPostProcessor>();
		result.addAll(sortAndGetPositivePostProcessors(toList(priorityCustomFieldPostProcessors)));
		result.addAll(toList(customFieldPostProcessors));
		return result;
	}

	/**
	 * Returns a list of all custom field post-processors registered with multibindings with positive order.
	 * The list is sorted according to the order.
	 * 
	 * @return
	 */
	public LinkedList<FieldPostProcessor> getLowPriorityFieldPostProcessors() {
		LinkedList<FieldPostProcessor> result = new LinkedList<FieldPostProcessor>();
		result.addAll(sortAndGetNegativePostProcessors(toList(priorityCustomFieldPostProcessors)));
		return result;
	}

	private List<? extends FieldPostProcessor> sortAndGetPositivePostProcessors(
			List<PriorityFieldPostProcessor> processors) {
		List<PriorityFieldPostProcessor> resultList = new ArrayList<PriorityFieldPostProcessor>();
		for (PriorityFieldPostProcessor processor : processors) {
			if (processor.getPriority() >= 0) {
				resultList.add(processor);
			}
		}
		sortByPriority(resultList);
		return resultList;
	}

	private List<? extends FieldPostProcessor> sortAndGetNegativePostProcessors(
			List<PriorityFieldPostProcessor> processors) {
		List<PriorityFieldPostProcessor> resultList = new ArrayList<PriorityFieldPostProcessor>();
		for (PriorityFieldPostProcessor processor : processors) {
			if (processor.getPriority() < 0) {
				resultList.add(processor);
			}
		}
		sortByPriority(resultList);
		return resultList;
	}

	private <T> List<T> toList(Set<T> set) {
		return set != null ? new ArrayList<T>(set) : Collections.<T> emptyList();
	}

	private <T extends PriorityProcessor> List<T> sortByPriority(List<T> processors) {
		Collections.sort(processors, PriorityProcessorComparator.INSTANCE);
		return processors;
	}

	private static class PriorityProcessorComparator implements Comparator<PriorityProcessor> {

		public final static Comparator<PriorityProcessor> INSTANCE = Collections
				.reverseOrder(new PriorityProcessorComparator());

		@Override
		public int compare(PriorityProcessor o1, PriorityProcessor o2) {
			return Integer.valueOf(o1.getPriority()).compareTo(o2.getPriority());
		}
	}
}
