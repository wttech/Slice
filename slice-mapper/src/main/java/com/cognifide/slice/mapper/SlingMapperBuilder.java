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
import com.cognifide.slice.mapper.impl.processor.BooleanFieldProcessor;
import com.cognifide.slice.mapper.impl.processor.DefaultFieldProcessor;
import com.cognifide.slice.mapper.impl.processor.SliceReferenceFieldProcessor;
import com.cognifide.slice.mapper.impl.processor.SliceResourceFieldProcessor;
import com.google.inject.Inject;

public final class SlingMapperBuilder extends MapperBuilder {

	private final SliceResourceFieldProcessor sliceResourceFieldProcessor;

	private final SliceReferenceFieldProcessor sliceReferenceFieldProcessor;

	@Inject
	public SlingMapperBuilder(final SliceResourceFieldProcessor sliceResourceFieldProcessor,
			final SliceReferenceFieldProcessor sliceReferenceFieldProcessor) {
		this.sliceResourceFieldProcessor = sliceResourceFieldProcessor;
		this.sliceReferenceFieldProcessor = sliceReferenceFieldProcessor;
	}

	public Mapper build() {
		processors.add(new BooleanFieldProcessor());
		processors.add(sliceResourceFieldProcessor);
		processors.add(sliceReferenceFieldProcessor);
		postProcessors.add(new EscapeValuePostProcessor());
		processors.add(new DefaultFieldProcessor());
		return new GenericSlingMapper(this);
	}

	public Mapper getMapper() {
		return build();
	}

}
