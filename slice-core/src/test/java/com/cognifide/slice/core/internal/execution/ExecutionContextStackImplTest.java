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

import com.cognifide.slice.api.exceptions.InvalidExecutionContextException;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExecutionContextStackImplTest {

	private ExecutionContextStackImpl contextStack;

	@Before
	public void setup() {
		contextStack = new ExecutionContextStackImpl("/content");
	}

	@Test
	public void testGetAbsolutePath() {
		String path = contextStack.getAbsolutePath("/examplePath");
		Assert.assertEquals(path, "/examplePath");
		path = contextStack.getAbsolutePath("examplePath");
		Assert.assertEquals(path, "/content/examplePath");

		contextStack.push(new ExecutionContextImpl("/secondContent"));
		Assert.assertEquals(contextStack.size(), 1);
		path = contextStack.getAbsolutePath("./examplePath");
		Assert.assertEquals(path, "/secondContent/examplePath");
		contextStack.pop();
		Assert.assertEquals(contextStack.size(), 0);
	}

	@Test(expected = InvalidExecutionContextException.class)
	public void testGetAbsolutePathException() {
		Assert.assertEquals(contextStack.size(), 0);
		contextStack.getAbsolutePath("./examplePath");
	}
}