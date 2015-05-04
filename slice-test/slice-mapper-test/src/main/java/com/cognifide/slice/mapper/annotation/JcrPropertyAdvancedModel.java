/*-
 * #%L
 * Slice - Mapper Tests
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
package com.cognifide.slice.mapper.annotation;

/**
 * @author Mariusz Kubiś Date: 10.04.15
 */

@SliceResource
public class JcrPropertyAdvancedModel {

	@JcrProperty
	private String text;

	@JcrProperty(value = "style")
	private String secondProperty;

	@JcrProperty
	private int size;

	@JcrProperty(value = "size")
	private final int sizeFinal = 2;

	@JcrProperty(value = "size")
	private static int sizeStatic = 4;

	@JcrProperty
	private long sizeLong; //FIXME this is not working properly for not exisitng properties

	@JcrProperty
	private double sizeDouble;

	@JcrProperty("sizeDouble")
	private float sizeFloat;

	@JcrProperty("sizeDouble")
	private short sizeShort;

	@JcrProperty("sizeLong")
	private Long sizeLongObject;

	@JcrProperty("sizeDouble")
	private Double sizeDoubleObject;

	@JcrProperty("sizeDouble")
	private Float sizeFloatObject;

	@JcrProperty("sizeDouble")
	private Short sizeShortObject;

	public String getText() {
		return text;
	}

	public String getSecondProperty() {
		return secondProperty;
	}

	public int getSize() {
		return size;
	}

	public int getSizeFinal() {
		return sizeFinal;
	}

	public static int getSizeStatic() {
		return sizeStatic;
	}

	public long getSizeLong() {
		return sizeLong;
	}

	public double getSizeDouble() {
		return sizeDouble;
	}

	public Long getSizeLongObject() {
		return sizeLongObject;
	}

	public Double getSizeDoubleObject() {
		return sizeDoubleObject;
	}

	public Float getSizeFloatObject() {
		return sizeFloatObject;
	}

	public Short getSizeShortObject() {
		return sizeShortObject;
	}

	public float getSizeFloat() {
		return sizeFloat;
	}

	public short getSizeShort() {
		return sizeShort;
	}
}
