package com.cognifide.slice.mapper;

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

import com.cognifide.slice.mapper.api.Mapper;
import com.cognifide.slice.mapper.impl.postprocessor.EscapeValuePostProcessor;
import com.cognifide.slice.mapper.impl.processor.ChildrenFieldProcessor;
import com.cognifide.slice.mapper.impl.processor.BooleanFieldProcessor;
import com.cognifide.slice.mapper.impl.processor.SliceReferenceFieldProcessor;
import com.cognifide.slice.mapper.impl.processor.SliceResourceFieldProcessor;
import com.google.inject.Inject;

public final class SlingMapperFactory {

	private final MapperFactory mapperFactory;

	private final SliceResourceFieldProcessor sliceResourceFieldProcessor;

	private final SliceReferenceFieldProcessor sliceReferenceFieldProcessor;
	
	private final ChildrenFieldProcessor childrenFieldProcessor;

	@Inject
	public SlingMapperFactory(final MapperFactory mapperFactory,
			final SliceResourceFieldProcessor sliceResourceFieldProcessor,
			final SliceReferenceFieldProcessor sliceReferenceFieldProcessor, 
	 		final ChildrenFieldProcessor childrenFieldProcessor) {
		this.mapperFactory = mapperFactory;
		this.sliceResourceFieldProcessor = sliceResourceFieldProcessor;
		this.sliceReferenceFieldProcessor = sliceReferenceFieldProcessor;
		this.childrenFieldProcessor = childrenFieldProcessor;
	}

	public Mapper getMapper() {
		final Mapper mapper = mapperFactory.getMapper();

		mapper.registerFieldProcessor(new BooleanFieldProcessor());
		mapper.registerFieldProcessor(childrenFieldProcessor);
		mapper.registerFieldProcessor(sliceResourceFieldProcessor);
		mapper.registerFieldProcessor(sliceReferenceFieldProcessor);
		mapper.registerFieldPostProcessor(new EscapeValuePostProcessor());

		return mapper;
	}

}
