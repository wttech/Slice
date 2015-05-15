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
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MapperBuilderTest {

	@Mock
	private FieldProcessor fieldProcessor;

	@Mock
	private FieldPostProcessor fieldPostProcessor;

	@Mock
	private SliceResourceFieldProcessor sliceResourceFieldProcessor;

	@Mock
	private SliceReferenceFieldProcessor sliceReferenceFieldProcessor;

	@Mock
	private ChildrenFieldProcessor childrenFieldProcessor;

	@InjectMocks
	private MapperBuilder mapperBuilder = new MapperBuilder();

	@Test
	public void shouldContainProcessorAtFirstPositionWhenAdded() {
		mapperBuilder.addSliceProcessors();

		mapperBuilder.addFieldProcessor(fieldProcessor);

		assertThat("number of processors should be different than 1",
				mapperBuilder.getProcessors().size(), is(not(1)));
		assertThat("processors should contain added processor",
				mapperBuilder.getProcessors().getFirst(), is(fieldProcessor));
	}

	@Test
	public void shouldContainFielPostProcessorAtFirstPositionWhenAdded() {
		mapperBuilder.addSlicePostProcessors();

		mapperBuilder.addFieldPostProcessor(fieldPostProcessor);

		assertThatContainsMoreThanOnePostProcessor();
		assertThatAddedPostProcessorIsFirst();
	}

	private void assertThatContainsMoreThanOnePostProcessor() {
		assertThat("number of post processors should be different than 1",
				mapperBuilder.getPostProcessors().size(), allOf(is(not(0)), is(not(1))));
	}

	private void assertThatAddedPostProcessorIsFirst() {
		assertThat("post processors should contain added post processor as first",
				mapperBuilder.getPostProcessors().getFirst(), is(fieldPostProcessor));
	}

	@Test
	public void shouldContainProcessorAtFirstPositionWhenAskedToAddAtFirstPosition() {
		mapperBuilder.addSlicePostProcessors();

		mapperBuilder.addFieldPostProcessorAsFirst(fieldPostProcessor);

		assertThatContainsMoreThanOnePostProcessor();
		assertThatAddedPostProcessorIsFirst();
	}

	@Test
	public void shouldContainProcessorAtLastPositionWhenAskedToAddAtLastPosition() {
		mapperBuilder.addSlicePostProcessors();

		mapperBuilder.addFieldPostProcessorAsLast(fieldPostProcessor);

		assertThatContainsMoreThanOnePostProcessor();
		assertThat("post processors should contain added post processo as last",
				mapperBuilder.getPostProcessors().getLast(), is(fieldPostProcessor));
	}

	@Test
	public void shouldContainAllSliceProcessorsAndPostProcessors() {
		mapperBuilder.addDefaultSliceProcessors();

		assertThatContainsAllProcessors();
		assertThatContainsAllPostProcessors();
	}

	private void assertThatContainsAllProcessors() {
		assertThat("number of processors should be 7", mapperBuilder.getProcessors().size(), is(7));
		Assert.assertEquals("slice reference field processor should be first",
				mapperBuilder.getProcessors().removeFirst(), sliceReferenceFieldProcessor);
		Assert.assertEquals("slice resource field processor should be second",
				mapperBuilder.getProcessors().removeFirst(), sliceResourceFieldProcessor);
		Assert.assertEquals("children field processor should be third",
				mapperBuilder.getProcessors().removeFirst(), childrenFieldProcessor);
		assertThat("list field processor should be fourth",
				mapperBuilder.getProcessors().removeFirst(), is(instanceOf(ListFieldProcessor.class)));
		assertThat("boolean field processor should be fifth",
				mapperBuilder.getProcessors().removeFirst(), is(instanceOf(BooleanFieldProcessor.class)));
		assertThat("enum field processor should be sixth",
				mapperBuilder.getProcessors().removeFirst(), is(instanceOf(EnumFieldProcessor.class)));
		assertThat("default field processor should be seventh",
				mapperBuilder.getProcessors().removeFirst(), is(instanceOf(DefaultFieldProcessor.class)));
	}

	private void assertThatContainsAllPostProcessors() {
		assertThat("number of post processors should be 1", mapperBuilder.getPostProcessors().size(), is(1));
		assertThat("escape value post processor should be first",
				mapperBuilder.getPostProcessors().removeFirst(), is(instanceOf(EscapeValuePostProcessor.class)));
	}

	@Test
	public void shouldContainAllSliceProcessors() {
		mapperBuilder.addSliceProcessors();

		assertThatContainsAllProcessors();
	}

	@Test
	public void shouldContainAllSlicePostProcessors() {
		mapperBuilder.addSlicePostProcessors();

		assertThatContainsAllPostProcessors();
	}

}
