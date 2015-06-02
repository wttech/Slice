package com.cognifide.slice.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import com.cognifide.slice.mapper.api.processor.FieldPostProcessor;
import com.cognifide.slice.mapper.api.processor.FieldProcessor;
import com.cognifide.slice.mapper.api.processor.PriorityFieldPostProcessor;
import com.cognifide.slice.mapper.api.processor.PriorityFieldProcessor;

@RunWith(MockitoJUnitRunner.class)
public class CustomProcessorsCollectorTest {

	private CustomProcessorsCollector testedObject;

	@Before
	public void setUp() {
		testedObject = new CustomProcessorsCollector();
	}

	@Test
	public void getFieldProcessors_noCustomProcessors_returnsEmptyList() throws Exception {
		noProcessors();

		LinkedList<FieldProcessor> actualResult = testedObject.getFieldProcessors();

		assertThat(actualResult).isEmpty();
	}

	@Test
	public void getFieldProcessors_customProcessorsSet_priorityProcessorsTakePrecedenceOverNonRpriorityProcessors()
			throws Exception {
		FieldProcessor nonPriorityProcessor = stubProcessor();
		PriorityFieldProcessor priorityProcessor = stubPriorityProcessor(0);
		setProcessors(nonPriorityProcessor);
		setPriorityProcessors(priorityProcessor);
		LinkedList<FieldProcessor> expectedResult = asList(priorityProcessor, nonPriorityProcessor);

		LinkedList<FieldProcessor> actualResult = testedObject.getFieldProcessors();

		assertThat(actualResult).isEqualTo(expectedResult);
	}

	@Test
	public void getFieldProcessors_multiplePriorityProcessors_returnsListSortedAccordingToPriority()
			throws Exception {
		PriorityFieldProcessor lowPriorityProcessor = stubPriorityProcessor(-1);
		PriorityFieldProcessor defaultPriorityProcessor = stubPriorityProcessor(0);
		PriorityFieldProcessor highPriorityProcessor = stubPriorityProcessor(1);
		setPriorityProcessors(lowPriorityProcessor, defaultPriorityProcessor, highPriorityProcessor);
		LinkedList<FieldProcessor> expectedResult = asList(highPriorityProcessor, defaultPriorityProcessor,
				lowPriorityProcessor);

		LinkedList<FieldProcessor> actualResult = testedObject.getFieldProcessors();

		assertThat(actualResult).isEqualTo(expectedResult);
	}

	@Test
	public void getHighPriorityFieldPostProcessors_noCustomProcessors_returnsEmptyList() throws Exception {
		noProcessors();

		LinkedList<FieldPostProcessor> actualResult = testedObject.getHighPriorityFieldPostProcessors();

		assertThat(actualResult).isEmpty();
	}

	@Test
	public void getHighPriorityFieldPostProcessors_customProcessorsSet_priorityProcessorsTakePrecedenceOverNonRpriorityProcessors()
			throws Exception {
		FieldPostProcessor nonPriorityProcessor = stubPostProcessor();
		PriorityFieldPostProcessor priorityProcessor = stubPriorityPostProcessor(0);
		setPostProcessors(nonPriorityProcessor);
		setPriorityPostProcessors(priorityProcessor);
		LinkedList<FieldPostProcessor> expectedResult = asList(priorityProcessor, nonPriorityProcessor);

		LinkedList<FieldPostProcessor> actualResult = testedObject.getHighPriorityFieldPostProcessors();

		assertThat(actualResult).isEqualTo(expectedResult);
	}

	@Test
	public void getHighPriorityFieldPostProcessors_multipleHighAndLowPriorityProcessors_returnsListOfHighPriorityProcessorsOnlySortedAccordingToPriority()
			throws Exception {
		PriorityFieldPostProcessor lowPriorityProcessor = stubPriorityPostProcessor(-1);
		PriorityFieldPostProcessor defaultPriorityProcessor = stubPriorityPostProcessor(0);
		PriorityFieldPostProcessor highPriorityProcessor = stubPriorityPostProcessor(1);
		setPriorityPostProcessors(lowPriorityProcessor, defaultPriorityProcessor, highPriorityProcessor);
		LinkedList<FieldPostProcessor> expectedResult = asList(highPriorityProcessor,
				defaultPriorityProcessor);

		LinkedList<FieldPostProcessor> actualResult = testedObject.getHighPriorityFieldPostProcessors();

		assertThat(actualResult).isEqualTo(expectedResult);
	}

	@Test
	public void getLowPriorityFieldPostProcessors_noCustomProcessors_returnsEmptyList() throws Exception {
		noProcessors();

		LinkedList<FieldPostProcessor> actualResult = testedObject.getLowPriorityFieldPostProcessors();

		assertThat(actualResult).isEmpty();
	}

	@Test
	public void getLowPriorityFieldPostProcessors_customProcessorsSet_returnsOnlyLowPriorityProcessorsSortedAccordingToPriority()
			throws Exception {
		PriorityFieldPostProcessor veryLowPriorityProcessor = stubPriorityPostProcessor(-2);
		PriorityFieldPostProcessor lowPriorityProcessor = stubPriorityPostProcessor(-1);
		PriorityFieldPostProcessor highPriorityProcessor = stubPriorityPostProcessor(1);
		setPriorityPostProcessors(veryLowPriorityProcessor, lowPriorityProcessor, highPriorityProcessor);
		LinkedList<FieldPostProcessor> expectedResult = asList(lowPriorityProcessor, veryLowPriorityProcessor);

		LinkedList<FieldPostProcessor> actualResult = testedObject.getLowPriorityFieldPostProcessors();

		assertThat(actualResult).isEqualTo(expectedResult);
	}

	private LinkedList<FieldProcessor> asList(FieldProcessor... processors) {
		return new LinkedList<FieldProcessor>(Arrays.asList(processors));
	}

	private LinkedList<FieldPostProcessor> asList(FieldPostProcessor... processors) {
		return new LinkedList<FieldPostProcessor>(Arrays.asList(processors));
	}

	private FieldProcessor stubProcessor() {
		return mock(FieldProcessor.class);
	}

	private PriorityFieldProcessor stubPriorityProcessor(int priority) {
		PriorityFieldProcessor processor = mock(PriorityFieldProcessor.class);
		when(processor.getPriority()).thenReturn(priority);
		return processor;
	}

	private FieldPostProcessor stubPostProcessor() {
		return mock(FieldPostProcessor.class);
	}

	private PriorityFieldPostProcessor stubPriorityPostProcessor(int priority) {
		PriorityFieldPostProcessor processor = mock(PriorityFieldPostProcessor.class);
		when(processor.getPriority()).thenReturn(priority);
		return processor;
	}

	private void noProcessors() {
		setProcessors();
		setPostProcessors();
		setPriorityProcessors();
		setPriorityPostProcessors();
	}

	private void setProcessors(FieldProcessor... processors) {
		Whitebox.setInternalState(testedObject, "customFieldProcessors",
				new HashSet<FieldProcessor>(Arrays.asList(processors)));
	}

	private void setPriorityProcessors(PriorityFieldProcessor... processors) {
		Whitebox.setInternalState(testedObject, "priorityCustomFieldProcessors",
				new HashSet<PriorityFieldProcessor>(Arrays.asList(processors)));
	}

	private void setPostProcessors(FieldPostProcessor... processors) {
		Whitebox.setInternalState(testedObject, "customFieldPostProcessors", new HashSet<FieldPostProcessor>(
				Arrays.asList(processors)));
	}

	private void setPriorityPostProcessors(PriorityFieldPostProcessor... processors) {
		Whitebox.setInternalState(testedObject, "priorityCustomFieldPostProcessors",
				new HashSet<PriorityFieldPostProcessor>(Arrays.asList(processors)));
	}
}
