/*-
 * #%L
 * Slice - Mapper Tests
 * %%
 * Copyright (C) 2012 Wunderman Thompson Technology
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

import org.apache.commons.lang.StringUtils;

/**
 * @author Mariusz Kubiś Date: 14.04.15
 */
@SliceResource
public class SliceReferenceModel {

	@JcrProperty
	private String text;

	@SliceReference("/content/foo/jcr:content")
	@JcrProperty
	private JcrPropertyModel propertyModel;

	public String getText() {
		return text;
	}

	public JcrPropertyModel getPropertyModel() {
		return propertyModel;
	}

}

@SliceResource
class SliceReferenceWithNoReferencedResource {

	@JcrProperty
	private String text;

	@SliceReference("/content/notexisting/jcr:content")
	@JcrProperty
	private JcrPropertyModel propertyModel;

	public String getText() {
		return text;
	}

	public JcrPropertyModel getPropertyModel() {
		return propertyModel;
	}

}

@SliceResource
class SliceReferenceModelWithEmptyPlaceholderReference {

	@JcrProperty
	private String text;

	@SliceReference("${a0_}")
	@JcrProperty
	private JcrPropertyModel propertyModel;

	public String getText() {
		return text;
	}

	public JcrPropertyModel getPropertyModel() {
		return propertyModel;
	}
}

@SliceResource
class EmptySliceReferenceModel {
	@SliceReference(StringUtils.EMPTY)
	@JcrProperty
	private JcrPropertyModel propertyModel;

	JcrPropertyModel getPropertyModel() {
		return propertyModel;
	}
}

@SliceResource
class SliceReferenceRelativePathModel {
	@SliceReference("propertyModel")
	@JcrProperty
	private JcrPropertyModel propertyModel;

	JcrPropertyModel getPropertyModel() {
		return propertyModel;
	}
}