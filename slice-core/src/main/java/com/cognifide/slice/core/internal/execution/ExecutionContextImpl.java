/*-
 * #%L
 * Slice - Core
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

package com.cognifide.slice.core.internal.execution;

import org.apache.sling.api.resource.Resource;

import com.cognifide.slice.api.execution.ExecutionContext;

public class ExecutionContextImpl implements ExecutionContext {

	private String path;

	private Resource resource;

	public ExecutionContextImpl(final String path) {
		this.path = path;
	}

	public ExecutionContextImpl(final Resource resource) {
		this.resource = resource;
	}

	@Override
	public String getPath() {
		if (resource != null) {
			return resource.getPath();
		} else {
			return path;
		}
	}

	public void setPath(final String path) {
		this.path = path;
	}

	@Override
	public Resource getResource() {
		return resource;
	}

	public void setResource(final Resource resource) {
		this.resource = resource;
	}

}
