package com.cognifide.slice.mapper.impl.processor;

import com.cognifide.slice.mapper.annotation.SliceReference;
import com.cognifide.slice.mapper.annotation.SliceResource;

@SuppressWarnings("unused")
public class TestObject {

	private String stringField;

	private boolean booleanField;

	private int intField;

	private TestAnnotated annotatedField;

	@SliceReference("test")
	private TestAnnotated fieldAnnotatedWithSliceReference;
}

@SliceResource
class TestAnnotated {

}
