package com.cognifide.slice.mapper;

/*
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
import com.cognifide.slice.mapper.api.Mapper;
import com.cognifide.slice.mapper.api.processor.FieldPostProcessor;
import com.cognifide.slice.mapper.api.processor.FieldProcessor;
import java.util.Deque;
import java.util.LinkedList;

public class MapperBuilder {

	protected final Deque<FieldProcessor> processors = new LinkedList<FieldProcessor>();

	protected final Deque<FieldPostProcessor> postProcessors = new LinkedList<FieldPostProcessor>();

	public Mapper build() {
		return new GenericSlingMapper(this);
	}

	public final MapperBuilder addFieldProcessor(FieldProcessor fieldProcessor) {
		if (!processors.contains(fieldProcessor)) {
			processors.addFirst(fieldProcessor);
		}
		return this;
	}

	public final MapperBuilder addFieldPostProcessor(FieldPostProcessor fieldPostProcessor) {
		if (!postProcessors.contains(fieldPostProcessor)) {
			postProcessors.addFirst(fieldPostProcessor);
		}
		return this;
	}

	final Deque<FieldProcessor> getProcessors() {
		return processors;
	}

	final Deque<FieldPostProcessor> getPostProcessors() {
		return postProcessors;
	}

}
