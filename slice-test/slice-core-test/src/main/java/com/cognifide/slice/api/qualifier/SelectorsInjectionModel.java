/*-
 * #%L
 * Slice - Core Tests
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
package com.cognifide.slice.api.qualifier;

import com.cognifide.slice.mapper.annotation.SliceResource;
import com.google.inject.Inject;

import java.util.List;

/**
 * User: krzysztof.ryk@solsoft.pl Date: 4/22/15 11:30 AM
 */
@SliceResource
public class SelectorsInjectionModel {

	private String[] selectors;

	private List<String> selectorsAsList;

	private String selectorsAsString;

	@Inject
	public SelectorsInjectionModel(@Selectors String[] selectors, @Selectors List<String> selectorsAsList,
			@SelectorString @Nullable String selectorsAsString) {
		this.selectors = selectors;
		this.selectorsAsList = selectorsAsList;
		this.selectorsAsString = selectorsAsString;
	}

	public String[] getSelectors() {
		return selectors;
	}

	public List<String> getSelectorsAsList() {
		return selectorsAsList;
	}

	public String getSelectorsAsString() {
		return selectorsAsString;
	}

}
