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
public class PrimitivesModel {

	@JcrProperty
	private int intProperty;

	@JcrProperty
	private boolean boolProperty;

	@JcrProperty
	private long longProperty;

	@JcrProperty("longProperty")
	private float floatProperty;

	@JcrProperty("longProperty")
	private double doubleProperty;

	public int getIntProperty() {
		return intProperty;
	}

	public boolean isBoolProperty() {
		return boolProperty;
	}

	public long getLongProperty() {
		return longProperty;
	}

	public float getFloatProperty() {
		return floatProperty;
	}

	public double getDoubleProperty() {
		return doubleProperty;
	}
}
