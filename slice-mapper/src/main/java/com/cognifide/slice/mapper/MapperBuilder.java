/*-
 * #%L
 * Slice - Mapper
 * $Id:$
 * $HeadURL:$
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

import com.cognifide.slice.mapper.api.Mapper;
import com.cognifide.slice.mapper.api.processor.FieldPostProcessor;
import com.cognifide.slice.mapper.api.processor.FieldProcessor;
import com.cognifide.slice.mapper.impl.postprocessor.EscapeValuePostProcessor;
import com.cognifide.slice.mapper.impl.processor.BooleanFieldProcessor;
import com.cognifide.slice.mapper.impl.processor.ChildrenFieldProcessor;
import com.cognifide.slice.mapper.impl.processor.DefaultFieldProcessor;
import com.cognifide.slice.mapper.impl.processor.EnumFieldProcessor;
import com.cognifide.slice.mapper.impl.processor.ListFieldProcessor;
import com.cognifide.slice.mapper.impl.processor.SliceReferenceFieldProcessor;
import com.cognifide.slice.mapper.impl.processor.SliceResourceFieldProcessor;
import com.google.inject.Inject;

import java.util.Deque;
import java.util.LinkedList;

/**
 * MapperBuilder replaced previous MapperFactory. It allows to set Field Processors and Post Processors and
 * then build instance of {@link Mapper}
 * 
 * @author maciej.matuszewski
 */
public final class MapperBuilder {

	private final Deque<FieldProcessor> processors = new LinkedList<FieldProcessor>();

	private final Deque<FieldPostProcessor> postProcessors = new LinkedList<FieldPostProcessor>();

	@Inject
	private SliceResourceFieldProcessor sliceResourceFieldProcessor;

	@Inject
	private SliceReferenceFieldProcessor sliceReferenceFieldProcessor;
	
	@Inject
	private ChildrenFieldProcessor childrenFieldProcessor;

	/**
	 * This method creates new instance of {@link GenericSlingMapper}. Field processors should be added before
	 * this method.
	 * 
	 * @return
	 */
	public Mapper build() {
		return new GenericSlingMapper(this);
	}

	/**
	 * Adds {@link FieldProcessor} at the beginning of processors list.
	 * 
	 * @param fieldProcessor
	 * @return
	 */
	public MapperBuilder addFieldProcessor(FieldProcessor fieldProcessor) {
		processors.addFirst(fieldProcessor);
		return this;
	}

	/**
	 * Adds {@link FieldPostProcessor} at the beginning of postProcessors list.
	 * 
	 * @param fieldPostProcessor
	 * @return
	 */
	public MapperBuilder addFieldPostProcessor(FieldPostProcessor fieldPostProcessor) {
		postProcessors.addFirst(fieldPostProcessor);
		return this;
	}

	/**
	 * Adds default processors and post processors - the same that was added in SlingMapperFactory
	 * 
	 * @return
	 */
	public MapperBuilder addDefaultSliceProcessors() {
		processors.add(sliceReferenceFieldProcessor); // @SliceReference
		processors.add(sliceResourceFieldProcessor); // @SliceResource
		processors.add(childrenFieldProcessor); // child models @Children 
		processors.add(new ListFieldProcessor()); // Subclasses of Collection<?> and arrays
		processors.add(new BooleanFieldProcessor()); // booleans
		processors.add(new EnumFieldProcessor()); // enums
		processors.add(new DefaultFieldProcessor()); // any other fields

		postProcessors.add(new EscapeValuePostProcessor());
		return this;
	}

	Deque<FieldProcessor> getProcessors() {
		return processors;
	}

	Deque<FieldPostProcessor> getPostProcessors() {
		return postProcessors;
	}

}
