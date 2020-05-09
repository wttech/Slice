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

package com.cognifide.slice.mapper;

import java.util.Deque;
import java.util.LinkedList;

import com.cognifide.slice.mapper.api.Mapper;
import com.cognifide.slice.mapper.api.processor.FieldPostProcessor;
import com.cognifide.slice.mapper.api.processor.FieldProcessor;
import com.cognifide.slice.mapper.api.processor.PriorityFieldPostProcessor;
import com.cognifide.slice.mapper.api.processor.PriorityFieldProcessor;
import com.cognifide.slice.mapper.impl.CustomProcessorsCollector;
import com.cognifide.slice.mapper.impl.postprocessor.EscapeValuePostProcessor;
import com.cognifide.slice.mapper.impl.processor.*;
import com.google.inject.Inject;
import com.google.inject.Module;

/**
 * MapperBuilder replaced previous MapperFactory. It allows to set Field Processors and Post Processors and
 * then build instance of {@link Mapper}.
 * 
 * @author maciej.matuszewski
 */
public final class MapperBuilder {

	private final LinkedList<FieldProcessor> processors = new LinkedList<FieldProcessor>();

	private final LinkedList<FieldPostProcessor> postProcessors = new LinkedList<FieldPostProcessor>();

	private boolean customProcessorsAdded;

	private boolean customPostProcessorsAdded;

	@Inject
	private SliceResourceFieldProcessor sliceResourceFieldProcessor;

	@Inject
	private SliceReferenceFieldProcessor sliceReferenceFieldProcessor;

	@Inject
	private ChildrenFieldProcessor childrenFieldProcessor;

	@Inject
	private RequestAttributeProcessor requestAttributeProcessor;

	@Inject
	private CustomProcessorsCollector customProcessorsCollector;

	/**
	 * This method creates new instance of {@link GenericSlingMapper}. Field processors should be added before
	 * this method.
	 * 
	 * @return created mapper
	 */
	public Mapper build() {
		return new GenericSlingMapper(this);
	}

	/**
	 * Adds {@link FieldProcessor} at the beginning of processors list.
	 * 
	 * @param fieldProcessor field processor to be added
	 * @return this builder
	 */
	public MapperBuilder addFieldProcessor(FieldProcessor fieldProcessor) {
		processors.addFirst(fieldProcessor);
		return this;
	}

	/**
	 * Adds {@link FieldPostProcessor} at the beginning of post-processors list.
	 * 
	 * @param fieldPostProcessor post processor to be added
	 * @return this builder
	 */
	public MapperBuilder addFieldPostProcessor(FieldPostProcessor fieldPostProcessor) {
		postProcessors.addFirst(fieldPostProcessor);
		return this;
	}

	/**
	 * Adds default processors and post processors at the end of processors and post-processors list.
	 * 
	 * @return this builder
	 */
	public MapperBuilder addDefaultSliceProcessors() {
		addSliceProcessors();
		addSlicePostProcessors();
		return this;
	}

	/**
	 * Adds default processors at the end of processors list.
	 * 
	 * @return this builder
	 */
	public MapperBuilder addSliceProcessors() {
		processors.add(sliceReferenceFieldProcessor); // @SliceReference
		processors.add(sliceResourceFieldProcessor); // @SliceResource
		processors.add(childrenFieldProcessor); // child models @Children
		processors.add(requestAttributeProcessor); // @RequestAttribute
		processors.add(new ListFieldProcessor()); // Subclasses of Collection<?> and arrays
		processors.add(new BooleanFieldProcessor()); // booleans
		processors.add(new EnumFieldProcessor()); // enums
		processors.add(new DefaultFieldProcessor()); // any other fields
		return this;
	}

	/**
	 * Adds default post-processors at the end of processors list.
	 * 
	 * @return this builder
	 */
	public MapperBuilder addSlicePostProcessors() {
		postProcessors.add(new EscapeValuePostProcessor());
		return this;
	}

	/**
	 * Adds field processors registered with multibindings to the list of processors. To register your own
	 * processors add in your {@link Module} following code:
	 * 
	 * <pre>
	 * protected void configure() {
	 * 	Multibinder&lt;FieldProcessor&gt; multibinder = Multibinder.newSetBinder(binder(), FieldProcessor.class);
	 * 	multibinder.addBinding().to(MyCustomFieldProcessor.class);
	 * }
	 * </pre>
	 * 
	 * All registered processors will be added at the beginning of the processors list. If you want to ensure
	 * the order of processors use in your binding {@link PriorityFieldProcessor} instead of
	 * {@link FieldProcessor} . The priority parameter is used to sort processors. Processors with higher
	 * priority will be placed at the beginning of processors list. Notice that all custom processors
	 * registered with multibindings always take precedence over those added with
	 * {@link #addSliceProcessors()}) and {@link #addFieldProcessor(FieldProcessor)}.
	 * 
	 * @return this builder
	 */
	public MapperBuilder addCustomProcessors() {
		customProcessorsAdded = true;
		return this;
	}

	/**
	 * Adds field post-processors registered with multibindings to the list of post-processors. To register
	 * your own post-processors add in your {@link Module} following code:
	 * 
	 * <pre>
	 * protected void configure() {
	 * 	Multibinder&lt;FieldProcessor&gt; multibinder = Multibinder.newSetBinder(binder(), FieldPostProcessor.class);
	 * 	multibinder.addBinding().to(MyCustomFieldPostProcessor.class);
	 * }
	 * </pre>
	 * 
	 * All registered post-processors will be added at the beginning of post-processors list. If you want to
	 * ensure the order of post-processors use in your binding {@link PriorityFieldPostProcessor} instead of
	 * {@link FieldPostProcessor}. Post-processors with priority greater or equal to 0 will be added at the
	 * beginning of post-processors list. All post-processors added with the priority lower than 0 will be added
	 * at the end of post-processors-list. Notice that all custom post-processors registered with
	 * multibindings are always added before or after those added with {@link #addSliceProcessors()}) and
	 * {@link #addFieldPostProcessor(FieldPostProcessor)}.
	 * 
	 * @return this builder
	 */
	public MapperBuilder addCustomPostProcessors() {
		customPostProcessorsAdded = true;
		return this;
	}

	Deque<FieldProcessor> getProcessors() {
		if (customProcessorsAdded) {
			processors.addAll(0, customProcessorsCollector.getFieldProcessors());
		}
		return processors;
	}

	Deque<FieldPostProcessor> getPostProcessors() {
		if (customPostProcessorsAdded) {
			postProcessors.addAll(0, customProcessorsCollector.getHighPriorityFieldPostProcessors());
			postProcessors.addAll(customProcessorsCollector.getLowPriorityFieldPostProcessors());
		}
		return postProcessors;
	}
}
