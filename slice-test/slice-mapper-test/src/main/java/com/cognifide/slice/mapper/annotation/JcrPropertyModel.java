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
public class JcrPropertyModel {

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
}

@SliceResource
class JcrPropertyModelComparable extends JcrPropertyModel implements Comparable<JcrPropertyModel> {

	// descending sort by size property
	@Override
	public int compareTo(JcrPropertyModel that) {
		return Integer.valueOf(that.getSize()).compareTo(this.getSize());
	}
}