/*-
 * #%L
 * Slice - Core
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

package com.cognifide.slice.core.internal.execution;

import java.util.LinkedList;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;

import com.cognifide.slice.api.exceptions.InvalidExecutionContextException;
import com.cognifide.slice.api.execution.ExecutionContext;
import com.cognifide.slice.api.execution.ExecutionContextStack;

/**
 * This is internal class, should not be used outside of framework.
 * 
 * @author witoldsz
 */
public final class ExecutionContextStackImpl implements ExecutionContextStack {

	private final Stack<ExecutionContext> items = new Stack<ExecutionContext>();

	private final String rootPath;

	public ExecutionContextStackImpl(final String rootPath) {
		this.rootPath = rootPath;
	}

	@Override
	public ExecutionContext peek() {
		return items.peek();
	}

	@Override
	public void push(final ExecutionContext executionContext) {
		items.push(executionContext);
	}

	@Override
	public ExecutionContext pop() {
		return items.pop();
	}

	@Override
	public int size() {
		return items.size();
	}

	private String getRootPath() {
		return rootPath;
	}

	private boolean isEmpty() {
		return items.isEmpty();
	}

	private String joinPath(final Object... args) {
		return StringUtils.join(args, "/").replace("//", "/");
	}
	
	public LinkedList<ExecutionContext> getItems() {
		return new LinkedList<ExecutionContext>(items);
	}

	@Override
	public String getAbsolutePath(final String path) {
		if (path.charAt(0) == '/') {
			return path;
		}

		if (!path.startsWith("./")) {
			return joinPath(getRootPath(), path);
		}

		if (isEmpty()) {
			throw new InvalidExecutionContextException("Relative path not allowed on top of ExecutionContext: " + path);
		}

		// remove "./" from path
		return joinPath(peek().getPath(), path.substring(2));
	}
}