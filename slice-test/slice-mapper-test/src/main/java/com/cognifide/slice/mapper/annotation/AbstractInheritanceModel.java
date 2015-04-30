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
 * @author Mariusz Kubiś Date: 15.04.15
 */
@SliceResource(MappingStrategy.ANNOTATED)
class AbstractInheritanceModelAnnotated {

	@JcrProperty
	private String field1;
	
	private String notAnnotated;

	public String getField1() {
		return field1;
	}

	public String getNotAnnotated() {
		return notAnnotated;
	}
}

@SliceResource(MappingStrategy.ALL)
class AbstractInheritanceModelAll {

	private String field1;
	
	@IgnoreProperty
	private String ignored;

	public String getField1() {
		return field1;
	}

	public String getIgnored() {
		return ignored;
	}
}

class AbstractInheritanceModelWithout {

	private String field1;

	public String getField1() {
		return field1;
	}
}
