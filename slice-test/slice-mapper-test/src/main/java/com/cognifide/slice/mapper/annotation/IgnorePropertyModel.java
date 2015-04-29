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
 * @author Mariusz Kubiś Date: 14.04.15
 */
@SliceResource(MappingStrategy.ALL)
class IgnorePropertyModelAll {

	private String text;

	@IgnoreProperty
	private String style;

	@IgnoreProperty
	private int size;

	public IgnorePropertyModelAll() {
		this.size = 10;
	}

	public String getText() {
		return text;
	}

	public String getStyle() {
		return style;
	}

	public int getSize() {
		return size;
	}
}

@SliceResource(MappingStrategy.ANNOTATED)
class IgnorePropertyModelAnnotated {

	@JcrProperty
	private String text;

	private String style;

	private int size;

	public IgnorePropertyModelAnnotated() {
		this.size = 10;
	}

	public String getText() {
		return text;
	}

	public String getStyle() {
		return style;
	}

	public int getSize() {
		return size;
	}
}
